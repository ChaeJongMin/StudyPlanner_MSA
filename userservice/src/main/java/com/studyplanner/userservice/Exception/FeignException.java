package com.studyplanner.userservice.Exception;

import org.springframework.http.HttpStatus;

public class FeignException extends CustomException {

    protected FeignException(String code, String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, code, message);
    }
}
