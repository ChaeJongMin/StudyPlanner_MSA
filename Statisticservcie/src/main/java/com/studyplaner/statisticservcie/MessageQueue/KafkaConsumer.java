package com.studyplaner.statisticservcie.MessageQueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studyplaner.statisticservcie.Entity.StatisticDetailEntity;
import com.studyplaner.statisticservcie.Entity.StatisticTotalEntity;
import com.studyplaner.statisticservcie.Error.CustomException;
import com.studyplaner.statisticservcie.Repository.StatisticTotalRepository;
import com.studyplaner.statisticservcie.Repository.StatisticDetailRepository;
import com.studyplaner.statisticservcie.Service.StatisticUtil;
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

    private final StatisticTotalRepository statisticTotalRepository;
    private final StatisticDetailRepository statisticDetailRepository;
    private final StatisticUtil statisticUtil;

        @Transactional
        @KafkaListener(topics = "todo-statistic-create",groupId = "Statistic-ConsumerGroupId-create")
        public void createTodo(String kafkaMessage){
        Map<Object,Object> kafkaConsumerMap= new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try{
            kafkaConsumerMap = objectMapper.readValue(kafkaMessage, new TypeReference<Map<Object,Object>>() {});
        } catch (JsonProcessingException e) {
            //따로 로직 처리 필요
        }

        long userId = Long.parseLong(String.valueOf((Integer)kafkaConsumerMap.get("userId")));
        String date = (String)kafkaConsumerMap.get("date");
        String convertDate= statisticUtil.getCurrentWeekOfMonth(date);
        Optional<StatisticTotalEntity> optionalEntity = statisticTotalRepository.findByUserId(userId);
        Optional<StatisticDetailEntity> detailEntity = statisticDetailRepository.findByUserIdAndDate(userId,convertDate);
        if(optionalEntity.isEmpty()){

            statisticTotalRepository.save(StatisticTotalEntity.builder()
                    .userId(userId)
                    .build());

            if(detailEntity.isEmpty()){
                statisticDetailRepository.save(StatisticDetailEntity.builder()
                        .date(convertDate)
                        .userId(userId)
                        .build());
            }

        } else {
            optionalEntity.get().updateCreateCnt("Add");
            detailEntity.get().updateCreateCount("Add");
        }

    }

    @Transactional
    @KafkaListener(topics = "todo-statistic-success",groupId = "Statistic-ConsumerGroupId-success")
    public void SuccessTodo(String kafkaMessage){
        Map<Object,Object> kafkaConsumerMap= new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try{
            kafkaConsumerMap = objectMapper.readValue(kafkaMessage, new TypeReference<Map<Object,Object>>() {});
        } catch (JsonProcessingException e) {
            //따로 로직 처리 필요
        }
        //241023 예외처리
        long userId = Long.parseLong(String.valueOf((Integer)kafkaConsumerMap.get("userId")));
        //kafka 예외 처리 로직 필요
        String date = (String)kafkaConsumerMap.get("date");
        String convertDate= statisticUtil.getCurrentWeekOfMonth(date);

        StatisticDetailEntity detailEntity = statisticDetailRepository.findByUserIdAndDate(userId,convertDate)
                .orElseThrow(()-> new CustomException ("NOT_FOUNDED_USER", "해당 유저는 없습니다."));

        detailEntity.updateCreateCount("Add");

    }


    @Transactional
    @KafkaListener(topics = "todo-statistic-delete", groupId = "Statistic-ConsumerGroupId-delete")
    public void deleteTodo(String kafkaMessage) {
        Map<Object, Object> kafkaConsumerMap = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            kafkaConsumerMap = objectMapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {});
        } catch (JsonProcessingException e) {
            // 예외 처리 로직 필요
        }

        long userId = Long.parseLong(String.valueOf((Integer) kafkaConsumerMap.get("userId")));
        String date = (String) kafkaConsumerMap.get("date");
        boolean isSuccess = (boolean) kafkaConsumerMap.get("isComplete");

        StatisticTotalEntity statisticTotalEntity = statisticTotalRepository.findByUserId(userId).orElse(null);
        StatisticDetailEntity statisticDetailEntity = statisticDetailRepository.findByUserIdAndDate(userId, date).orElse(null);

        if (statisticTotalEntity != null && statisticDetailEntity != null) {
            if (statisticTotalEntity.getCreateCount() > 0) {
                statisticTotalEntity.updateCreateCnt("Delete");
            }
            if (isSuccess || statisticTotalEntity.getSuccessCnt() > 0) {
                statisticTotalEntity.updateSuccessCnt("Delete");
            }

            if (statisticDetailEntity.getCreateCnt() > 0) {
                statisticDetailEntity.updateCreateCount("Delete");
            }
            if (isSuccess || statisticDetailEntity.getSuccessCnt() > 0) {
                statisticDetailEntity.updateSuccessCount("Delete");
            }
        } else {
            //별도의 처리 필요
        }
    }
}
