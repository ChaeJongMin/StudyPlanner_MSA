package com.studyplanner.userservice.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestUpdateUserDto {
    @NotNull(message = "Nickname cannot be null")
    @Size(min=4, max=20, message = "Nicknames must be at least 4 characters and no more than 20 characters long")
    private String nickname;
}
