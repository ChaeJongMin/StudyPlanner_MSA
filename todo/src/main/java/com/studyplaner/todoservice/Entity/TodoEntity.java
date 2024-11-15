package com.studyplaner.todoservice.Entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "todo")
public class TodoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String context;

    @Column(nullable = false)
    private long userId;

    @Column(nullable = false)
    private boolean isComplete;

    private LocalDate date; //yyyy-MM-dd

    @Builder
    public TodoEntity(String context, long userId,LocalDate date){
        this.context=context;
        this.userId=userId;
        this.isComplete = false;
        this.date = date;
    }

    public void updateContext(String context){
        this.context = context;
    }
    public void updateIsComplete(boolean flag){
        this.isComplete = false;
    }
}
