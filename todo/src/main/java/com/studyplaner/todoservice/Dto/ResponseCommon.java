package com.studyplaner.todoservice.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ResponseCommon {
    private String code;
    private String message;

    @Builder
    public ResponseCommon(String code, String message){
        this.code = code;
        this.message =message;
    }
}
