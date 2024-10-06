package com.studyplaner.todo.Entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class UserEntity {
    @Id
    private long id;

    @Column(nullable = false)
    private String userId;

    @Builder
    public UserEntity(Long id, String userId){
        this.id=id;
        this.userId=userId;
    }
}
