package com.studyplaner.todoservice.Dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RequestUpdateDetailTodoDto {

    private long todoId;
    private boolean isComplete;
    private String context;

}
