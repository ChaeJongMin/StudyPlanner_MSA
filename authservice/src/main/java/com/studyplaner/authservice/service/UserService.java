package com.studyplaner.authservice.service;

import com.studyplaner.authservice.Dto.RequestIssueDto;
import com.studyplaner.authservice.Dto.RequestJoinUserDto;
import jakarta.servlet.http.Cookie;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {

    Long save(RequestJoinUserDto requestJoinUserDto);

    boolean existUserByUserId(String target);

    Cookie issueToken(RequestIssueDto requestIssueDto);
}
