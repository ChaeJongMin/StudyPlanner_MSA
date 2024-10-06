package com.studyplaner.todo.Entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "todo")
public class TodoEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String context;

    @Column(nullable = false)
    private long userId;

    @Column(nullable = false)
    private boolean isComplete;
    //날짜 따로 추가 필요



    @Builder
    public TodoEntity(String context, long userId){
        this.context=context;
        this.userId=userId;
        this.isComplete = false;
    }

    public void update(String context){
        this.context = context;
    }

    public void complete(){
        this.isComplete = true;
    }
}
