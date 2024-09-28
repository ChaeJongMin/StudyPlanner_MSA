package com.studyplanner.userservice.service;

import com.studyplanner.userservice.dto.request.RequestSaveUserDto;
import com.studyplanner.userservice.dto.response.ResponseJoinForAuthServerDto;
import org.springframework.transaction.annotation.Transactional;


public interface UserService {

    Long save(RequestSaveUserDto requestSaveUserDto);

    ResponseJoinForAuthServerDto getUserByNickname(String nickname);

    @Transactional
    Long update(String nickname);

    @Transactional
    Long delete(long id);

}
