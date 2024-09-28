package com.studyplaner.authservice.Dto;

import com.studyplaner.authservice.Entity.UserEntity;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestJoinUserDto {

    @NotNull(message ="UserId cannot be null")
    private String userId;

    @NotNull(message = "Nickname cannot be null")
    private String password;

    @Builder
    public RequestJoinUserDto(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }


    public UserEntity toUserEntity(){
        return UserEntity.builder()
                .userId(userId)
                .password(password)
                .build();
    }
}
