package com.studyplanner.userservice.Exception;

import org.springframework.http.HttpStatus;

public class FeignException extends CustomException {

    protected FeignException(String code, String message) {
        super(HttpStatus.BAD_REQUEST, code, message);
    }
}
