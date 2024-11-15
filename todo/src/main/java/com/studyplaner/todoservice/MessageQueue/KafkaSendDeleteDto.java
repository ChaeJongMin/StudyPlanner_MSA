package com.studyplaner.todoservice.MessageQueue;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class KafkaSendDeleteDto {

    private int successCnt;
        private int totalCnt;
    private long userId;
    private String date;
}
