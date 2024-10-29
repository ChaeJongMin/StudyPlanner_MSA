package com.studyplanner.userservice.service;

import com.studyplanner.userservice.dto.request.RequestSaveUserDto;
import com.studyplanner.userservice.dto.request.RequsetStatisticDto;
import com.studyplanner.userservice.dto.response.ResponseJoinForAuthServerDto;
import org.springframework.transaction.annotation.Transactional;


public interface UserService {

    Long save(RequestSaveUserDto requestSaveUserDto);

    ResponseJoinForAuthServerDto getUserByNickname(String nickname);


    Long update(long id,String nickname);

    RequsetStatisticDto getStatistic(long id);

    Long delete(long id);

}
