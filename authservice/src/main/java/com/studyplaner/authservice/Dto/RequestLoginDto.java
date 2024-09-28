package com.studyplaner.authservice.Dto;


import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class RequestLoginDto {
    private String userId;
    private String password;
}
