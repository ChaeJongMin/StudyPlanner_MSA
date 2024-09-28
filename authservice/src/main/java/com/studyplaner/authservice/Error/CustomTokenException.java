package com.studyplaner.authservice.Error;

public class CustomTokenException extends RuntimeException {
    private final String errorCode; // 문자열 코드 필드 추가

    public CustomTokenException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode; // 코드 저장
    }

    public String getErrorCode() {
        return errorCode; // 코드 반환 메서드 추가
    }
}