package com.studyplaner.authservice.service;

import com.studyplaner.authservice.Dto.RequestIssueDto;
import com.studyplaner.authservice.Dto.RequestJoinUserDto;
import com.studyplaner.authservice.Dto.UserDto;
import com.studyplaner.authservice.Entity.UserEntity;
import com.studyplaner.authservice.Repository.UserRepository;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final TokenUtil tokenUtil;
    private final CookieUtil cookieUtil;

    @Override
    public Long save(RequestJoinUserDto requestJoinUserDto) {
        UserEntity userEntity = userRepository.save(requestJoinUserDto.toUserEntity());
        return userEntity.getId();
    }

    @Override
    public boolean existUserByUserId(String target) {

        return userRepository.existsByUserId(target);
    }

    @Override
    public Cookie issueToken(RequestIssueDto requestIssueDto) {
        boolean tokenKind = requestIssueDto.isTokenKind();

        String accessToken = tokenUtil.doGenerateToken(requestIssueDto.getUserId(),TokenUtil.TOKEN_VALIDATION_SECOND);

        return cookieUtil.createCookie(TokenUtil.ACCESS_TOKEN,accessToken);
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        UserDto userDto =userRepository.findByUserId(userId)
                .orElseThrow(()-> new UsernameNotFoundException("해당 유저는 없습니다."));

        return new User(userDto.getUserId(), userDto.getPassword(), true, true, true, true,
                new ArrayList<>());
    }

}
