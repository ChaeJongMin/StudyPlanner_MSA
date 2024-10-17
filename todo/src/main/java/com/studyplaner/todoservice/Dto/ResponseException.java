package com.studyplaner.todoservice.Dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseException {
    private String code;
    private String message;
}
