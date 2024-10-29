package com.studyplaner.todoservice.MessageQueue;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class KakfaSendDto {

    private long userId;
    private String date;

    public KakfaSendDto(long userId, String date){
        this.userId = userId;
        this.date = date;
    }
}
