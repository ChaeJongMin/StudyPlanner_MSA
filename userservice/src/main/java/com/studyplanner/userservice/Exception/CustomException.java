package com.studyplanner.userservice.Exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import javax.xml.transform.Result;

@Getter
public class CustomException extends RuntimeException{
    private final HttpStatus status;
    private final String code;
    private final String message;

    protected CustomException(HttpStatus status, String code,String message) {
        this.status = status;
        this.code = code;
        this.message = message;

    }
}
