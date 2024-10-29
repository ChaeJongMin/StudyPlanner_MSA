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
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonToMessage = "";
        //userId와 날짜
        try{
            jsonToMessage = objectMapper.writeValueAsString(kakfaSendDto);
        } catch (JsonProcessingException ex){
            ex.printStackTrace();
        }
        String topic="";
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
        String topic = "";
        kafkaTemplate.send(topic,jsonToMessage);
    }

}
