package com.studyplaner.statisticservcie.MessageQueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studyplaner.statisticservcie.Entity.StatisticEntity;
import com.studyplaner.statisticservcie.Entity.StatisticTodoEntity;
import com.studyplaner.statisticservcie.Repository.StatisticRepository;
import com.studyplaner.statisticservcie.Repository.StatisticTodoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {

    private final StatisticRepository statisticRepository;
    private final StatisticTodoRepository statisticTodoRepository;

    @Transactional
    @KafkaListener(topics = "")
    public void updateStatistic(String kafkaMessage){
        Map<Object,Object> kafkaConsumerMap= new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try{
            kafkaConsumerMap = objectMapper.readValue(kafkaMessage, new TypeReference<Map<Object,Object>>() {});
        } catch (JsonProcessingException e) {
            //따로 로직 처리 필요
        }
        long userId = (Long)kafkaConsumerMap.get("userPk");
        //kafka 예외 처리 로직 필요
        Optional<StatisticTodoEntity> statisticTodoEntity = statisticTodoRepository.findByUserId(userId);

        //일,주,월 달성률 처리
        String date = ((String)kafkaConsumerMap.get("date"));
        if(statisticTodoEntity.isEmpty()){
            statisticTodoRepository.save(new StatisticTodoEntity(userId,date));
        } else {
            statisticTodoEntity.get().update();
        }
    }
}
