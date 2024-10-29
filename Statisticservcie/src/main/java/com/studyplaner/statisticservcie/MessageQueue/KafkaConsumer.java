package com.studyplaner.statisticservcie.MessageQueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studyplaner.statisticservcie.Entity.StatisticEntity;
import com.studyplaner.statisticservcie.Entity.StatisticTodoEntity;
import com.studyplaner.statisticservcie.Lock.SharedState;
import com.studyplaner.statisticservcie.Repository.StatisticRepository;
import com.studyplaner.statisticservcie.Repository.StatisticTodoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {

    private final StatisticRepository statisticRepository;
    private final StatisticTodoRepository statisticTodoRepository;
    private final SharedState sharedState;

        @Transactional
        @KafkaListener(topics = "")
        public void createTodo(String kafkaMessage){
        Map<Object,Object> kafkaConsumerMap= new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try{
            kafkaConsumerMap = objectMapper.readValue(kafkaMessage, new TypeReference<Map<Object,Object>>() {});
        } catch (JsonProcessingException e) {
            //따로 로직 처리 필요
        }

        long userId = (Long)kafkaConsumerMap.get("userPk");
        //241023 예외처리
        Optional<StatisticEntity> statisticEntity = statisticRepository.findByUserId(userId);

        String date = (String)kafkaConsumerMap.get("date");
        if(statisticEntity.isPresent()){
            statisticEntity.get().updateTotalCnt();
        } else {
            StatisticTodoEntity statisticTodoEntity = new StatisticTodoEntity(userId,date);
            StatisticEntity statistic = new StatisticEntity(userId, statisticTodoEntity);

            statisticTodoRepository.save(statisticTodoEntity);
            statisticRepository.save(statistic);

        }
    }

    @Transactional
    @KafkaListener(topics = "")
    public void SuccessTodo(String kafkaMessage){
        Map<Object,Object> kafkaConsumerMap= new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        LocalDate today = LocalDate.now();
        // 원하는 형식으로 포맷터를 생성합니다.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd:HH-mm-ss");
        // 날짜를 포맷팅합니다.
        String formattedDate = today.format(formatter);

        try{
            kafkaConsumerMap = objectMapper.readValue(kafkaMessage, new TypeReference<Map<Object,Object>>() {});
        } catch (JsonProcessingException e) {
            //따로 로직 처리 필요
        }
        //241023 예외처리
        long userId = (Long)kafkaConsumerMap.get("userPk");
        //kafka 예외 처리 로직 필요
        Optional<StatisticTodoEntity> statisticTodoEntity = statisticTodoRepository.findByUserId(userId);

        //일,주,월 달성률 처리
        String date = ((String)kafkaConsumerMap.get("date"));
        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);

        LocalTime time = dateTime.toLocalTime();

        // 23시 59분 45초를 기준으로 설정
        LocalTime threshold = LocalTime.of(23, 59, 45);

        if(time.isAfter(threshold) && sharedState.isFlag()){ //특정 시간 이후면
            sharedState.getEntityQueue().add(statisticTodoEntity);

        } else {
            //                if(!date.equals(formattedDate)){
            //                    statisticTodoEntity.get().updateDateAndCnt(formattedDate,1);
            //                } else {
            //                    statisticTodoEntity.get().update();
            //                }
            statisticTodoEntity.ifPresent(StatisticTodoEntity::update);
            if(!sharedState.isFlag() && !sharedState.emptyCheck())
                sharedState.processQueue();
        }
    }
}
