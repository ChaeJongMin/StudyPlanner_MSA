package com.studyplanner.userservice.dto.request;

//import com.musicweb.userservice.domain.UserEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestSaveUserDto {

    //id
    @NotNull(message = "UserId cannot be null")
    @Size(min=4, message = "UserId must be at least 4 characters")
    private String userId;

    //비밀번호
    @NotNull(message = "Password cannot be null")
    @Size(min=4, message = "Passwrod must be at least 4 characters")
    private String password;

    //닉네임
    @NotNull(message = "Nickname cannot be null")
    @Size(min=4, max=20, message = "Nicknames must be at least 4 characters and no more than 20 characters long")
    private String nickname;

    public void encodePassword(String convertPassword){
        this.password = convertPassword;
    }

//    public UserEntity toUserEntity(){
//        return UserEntity.builder()
//                .userId(userId)
//                .password(password)
//                .nickname(nickname)
//                .build();
//    }

}
