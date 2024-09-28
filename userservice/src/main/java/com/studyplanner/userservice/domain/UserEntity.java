//package com.studyplanner.userservice.domain;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import lombok.AccessLevel;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
////@Entity
//@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//public class UserEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private long id;
//
//    private String userId;
//    private String password;
//    private String nickname;
//
//    public void update(String nickname){
//        this.nickname = nickname;
//    }
//
//    @Builder
//    public UserEntity(String userId, String password, String nickname){
//        this.userId = userId;
//        this.password = password;
//        this.nickname = nickname;
//    }
//}