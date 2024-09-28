package com.studyplanner.userservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseException {

    @JsonProperty("status")
    private int status;

    @JsonProperty("code")
    private String code;

    @JsonProperty("message")
    private String message;

    public ResponseException(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
