package com.studyplanner.userservice.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResponseJoinForAuthServerDto {
    @NotNull(message ="UserId cannot be null")
    private String userId;

    private String password;

    @Builder
    public ResponseJoinForAuthServerDto(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }
}
