package com.studyplaner.todoservice.MessageQueue;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
public class KakfaSendDto {

    private long userId;
    private String date;
    private int count;

}
