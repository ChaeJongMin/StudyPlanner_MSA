package com.studyplaner.todoservice.MessageQueue;


import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class KafkaSendUpdateDto {

    private int successCnt;
    private String date;
    private long userId;
}
