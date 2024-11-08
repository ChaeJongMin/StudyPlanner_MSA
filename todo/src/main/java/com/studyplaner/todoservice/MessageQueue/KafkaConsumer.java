package com.studyplaner.todoservice.MessageQueue;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studyplaner.todoservice.Entity.UserEntity;
import com.studyplaner.todoservice.Error.NotFoundUserOrTodoException;
import com.studyplaner.todoservice.Repository.UserRepository;
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

    private final UserRepository userRepository;

    @Transactional
    @KafkaListener(topics = "user-todo-event", groupId = "Todo-ConsumerGroupId-user")
    public void updateUser(String kafkaMessage){
        Map<Object,Object> kafkaConsumerMap= new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try{
            kafkaConsumerMap = objectMapper.readValue(kafkaMessage, new TypeReference<Map<Object,Object>>() {});
        } catch (JsonProcessingException e) {
            //따로 로직 처리 필요
        }

        //kafka 예외 처리 로직 필요
        UserEntity userEntity = userRepository.findById((Long)kafkaConsumerMap.get("userPk"))
                .orElseThrow(()-> new NotFoundUserOrTodoException("Not_Found_User","해당 유저는 없습니다."));

        userEntity.update((String)kafkaConsumerMap.get("userId"));
    }

}
