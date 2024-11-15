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
        int count = (Integer)kafkaConsumerMap.get("count");

        String convertDate= statisticUtil.getCurrentWeekOfMonth(date);
        Optional<StatisticTotalEntity> optionalEntity = statisticTotalRepository.findByUserId(userId);
        Optional<StatisticDetailEntity> detailEntity = statisticDetailRepository.findByUserIdAndDate(userId,convertDate);
        if(optionalEntity.isEmpty() && detailEntity.isEmpty()){

            statisticTotalRepository.save(StatisticTotalEntity.builder()
                    .userId(userId)
                    .createCount(count)
                    .build());

            statisticDetailRepository.save(StatisticDetailEntity.builder()
                    .date(convertDate)
                    .userId(userId)
                    .createCnt(count)
                    .build());

        } else {
            optionalEntity.get().updateCreateCnt(count);
            detailEntity.get().updateCreateCount(count);
        }

    }

    @Transactional
    @KafkaListener(topics = "todo-statistic-success",groupId = "Statistic-ConsumerGroupId-success")
    public void SuccessTodo(String kafkaMessage){
        log.info("할일 완료 메시지를 받았습니다.");
        Map<Object,Object> kafkaConsumerMap= new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try{
            kafkaConsumerMap = objectMapper.readValue(kafkaMessage, new TypeReference<Map<Object,Object>>() {});
        } catch (JsonProcessingException e) {
            //따로 로직 처리 필요
        }
        //241023 예외처리
        long userId = Long.parseLong(String.valueOf((Integer)kafkaConsumerMap.get("userId")));
        int count = (Integer)kafkaConsumerMap.get("successCnt");

        //kafka 예외 처리 로직 필요
        String date = (String)kafkaConsumerMap.get("date");
        String convertDate= statisticUtil.getCurrentWeekOfMonth(date);

        StatisticTotalEntity totalEntity = statisticTotalRepository.findByUserId(userId)
                .orElseThrow(()-> new CustomException ("NOT_FOUNDED_USER", "해당 유저는 없습니다."));

        StatisticDetailEntity detailEntity = statisticDetailRepository.findByUserIdAndDate(userId,convertDate)
                .orElseThrow(()-> new CustomException ("NOT_FOUNDED_USER", "해당 유저는 없습니다."));

        totalEntity.updateSuccessCnt(count);
        detailEntity.updateSuccessCount(count);

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
        //메시지가 없을 떄 예외 처리 필요 .KafkaException
        long userId = Long.parseLong(String.valueOf((Integer) kafkaConsumerMap.get("userId")));
        String date = (String) kafkaConsumerMap.get("date");
        int successCnt = (Integer)kafkaConsumerMap.get("successCnt");
        int totalCnt = (Integer)kafkaConsumerMap.get("totalCnt");

        String convertDate= statisticUtil.getCurrentWeekOfMonth(date);

        String isSuccessStr = (String) kafkaConsumerMap.get("isComplete");

        StatisticTotalEntity statisticTotalEntity = statisticTotalRepository.findByUserId(userId).orElse(null);
        StatisticDetailEntity statisticDetailEntity = statisticDetailRepository.findByUserIdAndDate(userId, convertDate).orElse(null);

        boolean isSuccess = isSuccessStr.equals("success");
        log.info("삭제 메시지 isSuccess : " +isSuccess+" "+isSuccessStr);
        if (statisticTotalEntity != null && statisticDetailEntity != null) {
            log.info("삭제 처리 시작");
            if (statisticTotalEntity.getCreateCount() > 0) {
                statisticTotalEntity.updateCreateCnt(totalCnt);
            }
            if (isSuccess && statisticTotalEntity.getSuccessCnt() > 0) {
                statisticTotalEntity.updateSuccessCnt(successCnt);
            }

            if (statisticDetailEntity.getCreateCnt() > 0) {
                statisticDetailEntity.updateCreateCount(totalCnt);
            }
            if (isSuccess && statisticDetailEntity.getSuccessCnt() > 0) {
                statisticDetailEntity.updateSuccessCount(successCnt);
            }
        } else {
            //별도의 처리 필요
        }
    }
}
