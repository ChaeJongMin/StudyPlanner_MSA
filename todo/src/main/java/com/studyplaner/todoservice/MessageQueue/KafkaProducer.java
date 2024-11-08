package com.studyplaner.todoservice.MessageQueue;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String,String> kafkaTemplate;

    //통계서비스에 1. todo 생성 2. todo 클리어 정보 전달

    public void sendTodoCreate(KakfaSendDto kakfaSendDto){
        log.info("sendTodoCreate 메시지가 토픽으로 전달됩니다.");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonToMessage = "";
        //userId와 날짜
        try{
            jsonToMessage = objectMapper.writeValueAsString(kakfaSendDto);
        } catch (JsonProcessingException ex){
            ex.printStackTrace();
        }
        String topic="todo-statistic-create";
        kafkaTemplate.send(topic,jsonToMessage);
    }

    public void sendTodoSuccess(KakfaSendDto kakfaSendDto){
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonToMessage = "";
        try{
            jsonToMessage = objectMapper.writeValueAsString(kakfaSendDto);
        } catch (JsonProcessingException ex){
            ex.printStackTrace();
        }
        String topic = "todo-statistic-success";
        kafkaTemplate.send(topic,jsonToMessage);
    }

}
