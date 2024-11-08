package com.studyplanner.userservice.MessageQueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studyplanner.userservice.dto.response.ResponseJoinForAuthServerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendRegisterUser(ResponseJoinForAuthServerDto responseJoinForAuthServerDto){
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonToMessage = "user-auth-event";
        //userId와 날짜
        try{
            jsonToMessage = objectMapper.writeValueAsString(responseJoinForAuthServerDto);
        } catch (JsonProcessingException ex){
            ex.printStackTrace();
        }
        String topic="";
        kafkaTemplate.send(topic,jsonToMessage);
    }

    public void sendUpdateNicKName(KafkaSendDto kakfaSendDto){
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonToMessage = "user-todo-event";
        //userId와 날짜
        try{
            jsonToMessage = objectMapper.writeValueAsString(kakfaSendDto);
        } catch (JsonProcessingException ex){
            ex.printStackTrace();
        }
        String topic="";
        kafkaTemplate.send(topic,jsonToMessage);

    }
}