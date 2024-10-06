package com.studyplaner.todo.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestUpdateTodoDto {
    private String userId;
    private String context;

}
