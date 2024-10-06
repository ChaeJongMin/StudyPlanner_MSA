package com.studyplaner.authservice.Error;

import lombok.Getter;

@Getter
public class CustomTokenException extends RuntimeException {
    // 코드 반환 메서드 추가
    private final String errorCode; // 문자열 코드 필드 추가

    public CustomTokenException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode; // 코드 저장
    }

}