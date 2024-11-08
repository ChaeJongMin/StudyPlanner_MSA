package com.studyplaner.authservice.MessageQueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studyplaner.authservice.Dto.RequestJoinUserDto;
import com.studyplaner.authservice.service.UserServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {

    private final UserServiceImpl userService;

    @KafkaListener(topics = "user-auth-event", groupId = "Auth-ConsumerGroupId-Register")
    public void createUser(String kafkaMessage){
        Map<Object,Object> kafkaConsumerMap= new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try{
            kafkaConsumerMap = objectMapper.readValue(kafkaMessage, new TypeReference<Map<Object,Object>>() {});
        } catch (JsonProcessingException e) {
            //따로 로직 처리 필요
        }
        String userId =kafkaConsumerMap.get("userId").toString();
        String password =kafkaConsumerMap.get("password").toString();

        RequestJoinUserDto requestJoinUserDto = new RequestJoinUserDto(userId,password);

        long result = userService.save(requestJoinUserDto);
    }
}
