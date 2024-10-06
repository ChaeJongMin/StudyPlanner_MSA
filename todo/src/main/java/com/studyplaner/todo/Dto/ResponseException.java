package com.studyplaner.todo.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
public class ResponseException {
    private String code;
    private String message;
}
