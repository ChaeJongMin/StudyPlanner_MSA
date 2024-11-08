package com.studyplaner.statisticservcie.Error;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{

    private String code = "";
    private String message = "";

    public CustomException(String code, String message){
        super(message);
        this.code = code;
        this.message = message;
    }
}
