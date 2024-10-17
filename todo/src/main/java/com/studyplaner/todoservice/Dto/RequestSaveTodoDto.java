package com.studyplaner.todoservice.Dto;

import com.studyplaner.todoservice.Entity.TodoEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestSaveTodoDto {
    private String userId;
    private String context;

    public TodoEntity toTodoEntity(long userId){
        return TodoEntity.builder()
                .userId(userId)
                .context(this.context)
                .build();
    }
}
