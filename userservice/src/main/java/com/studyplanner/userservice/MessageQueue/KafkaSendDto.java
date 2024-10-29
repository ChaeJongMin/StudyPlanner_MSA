package com.studyplanner.userservice.MessageQueue;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KafkaSendDto {

    public long id;
    public String nickname;


    public KafkaSendDto(long id, String nickname){
        this.id = id;
        this.nickname = nickname;
    }
}
