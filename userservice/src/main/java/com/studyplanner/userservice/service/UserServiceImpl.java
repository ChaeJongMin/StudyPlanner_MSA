package com.studyplanner.userservice.service;

import com.studyplanner.userservice.Exception.DuplicateUserInfo;
import com.studyplanner.userservice.Feign.AuthServiceClient;
import com.studyplanner.userservice.dto.request.RequestSaveUserDto;
import com.studyplanner.userservice.dto.response.ResponseJoinForAuthServerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
@Slf4j
public class UserServiceImpl implements UserService{

    private final PasswordEncoder passwordEncoder;
    private final AuthServiceClient authServiceClient;

//    @Transactional
    @Override
    public Long save(RequestSaveUserDto requestSaveUserDto) {

        String userId = requestSaveUserDto.getUserId();
        String nickname = requestSaveUserDto.getNickname();
        //아이디 닉네임 중복 예외 처리
//        if(userRepository.existsByUserId(userId)){
//            throw new DuplicateUserInfo("Duplicate User-id", "The user-id is already in use by another user.");
//        }
//        if(userRepository.existsByNickname(nickname)){
//            throw new DuplicateUserInfo("Duplicate Nickname", "The nickname is already in use by another user.");
//        }
        String password = requestSaveUserDto.getPassword();
        //객체 생성 후 save
        requestSaveUserDto.encodePassword(passwordEncoder.encode(password));

//        UserEntity userEntity = userRepository.save(requestSaveUserDto.toUserEntity());

        //feign 클라이언트로 전달
        log.info("[Jong]auth-service한테 회원가입한 유저 정보 전달");
        authServiceClient.responseSuccessJoin(new ResponseJoinForAuthServerDto(userId, requestSaveUserDto.getPassword()));

        return 1L;

    }

    @Override
    public ResponseJoinForAuthServerDto getUserByNickname(String nickname) {
        return null;
    }

    @Override
    public Long update(String nickname) {
        return 1L;
    }

    @Override
    public Long delete(long id) {
        return 0L;
    }
}
