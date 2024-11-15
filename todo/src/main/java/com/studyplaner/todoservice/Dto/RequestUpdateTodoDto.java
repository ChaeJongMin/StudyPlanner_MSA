package com.studyplaner.todoservice.Dto;

import com.studyplaner.todoservice.Entity.TodoEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class RequestUpdateTodoDto {
    private long userId;
    private String date;
    private List<RequestUpdateDetailTodoDto> detailList;
}


