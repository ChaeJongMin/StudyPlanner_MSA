package com.studyplaner.todoservice.Error;

import lombok.Getter;

@Getter
public class NotFoundUserOrTodoException extends RuntimeException{

    private final String code;
    private String message;

    public NotFoundUserOrTodoException(String code, String message){
        super(message);
        this.code=code;
    }
}
