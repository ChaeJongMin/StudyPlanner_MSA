package com.studyplanner.userservice.Exception;

import feign.Response;
import feign.codec.ErrorDecoder;

//FeginClient로 타 서비스 요청 시 발생한 예외를 처리하기 위한 클래스
public class FeginErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()){
            case 400:
                break;
            case 404:
                //메소드 getReviews 요청 시 발생한 예외를 처리
                if (methodKey.contains("responseSuccessJoin")) {
                    //예외 발생
                    return new FeignException("USER_REGISTRATION_FAILED", "User registration failed");
                }
                break;
            default:
                return new Exception(response.reason());
        }
        return null;
    }
}