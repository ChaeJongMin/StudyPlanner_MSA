package com.studyplaner.todoservice.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.studyplaner.todoservice.Entity.TodoEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class RequestSaveTodoDto {
    private String userId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MMM-dd", timezone = "Asia/Seoul")
    private LocalDate date;

    private List<String> contextList;

    public TodoEntity toTodoEntity(long userId,String context){
        return TodoEntity.builder()
                .userId(userId)
                .context(context)
                .date(this.date)
                .build();
    }
}
