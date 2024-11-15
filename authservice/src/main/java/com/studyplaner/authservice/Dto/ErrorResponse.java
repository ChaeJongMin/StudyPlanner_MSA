package com.studyplaner.authservice.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor

public class ErrorResponse {
    private String code;
    private String message;

    @Builder
    public ErrorResponse(String code,String message){
        this.code = code;
        this.message = message;
    }

}
