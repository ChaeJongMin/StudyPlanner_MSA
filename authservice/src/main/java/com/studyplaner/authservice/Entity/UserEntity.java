package com.studyplaner.authservice.Entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 20, nullable = false)
    private String userId;

    @Column(length = 20, nullable = false)
    private String password;

//    @Temporal(TemporalType.TIMESTAMP)
//    @CreationTimestamp
//    private Date createAt;
//
//    @Temporal(TemporalType.TIMESTAMP)
//    @UpdateTimestamp
//    private Date updateAt;

    @Builder
    public UserEntity(String userId, String password){
        this.userId = userId;
        this.password = password;
    }
}
