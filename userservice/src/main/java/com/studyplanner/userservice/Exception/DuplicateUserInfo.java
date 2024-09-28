package com.studyplanner.userservice.Exception;

import org.springframework.http.HttpStatus;

public class DuplicateUserInfo extends CustomException{

    public DuplicateUserInfo(String code, String message) {
        super(HttpStatus.CONFLICT, code, message);
    }
}
