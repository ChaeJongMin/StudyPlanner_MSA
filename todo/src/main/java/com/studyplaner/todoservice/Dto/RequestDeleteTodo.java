package com.studyplaner.todoservice.Dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class RequestDeleteTodo {

    private long userId;
    private String date;
    private List<Long> todoIdList;
}
