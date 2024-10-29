package com.studyplaner.authservice.service;

import com.studyplaner.authservice.Dto.RequestIssueDto;
import com.studyplaner.authservice.Dto.RequestJoinUserDto;
import com.studyplaner.authservice.Dto.UserDto;
import com.studyplaner.authservice.Entity.UserEntity;
import com.studyplaner.authservice.Error.CustomTokenException;
import com.studyplaner.authservice.Repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final TokenUtil tokenUtil;
    private final CookieUtil cookieUtil;
    private final RedisUtil redisUtil;

    @Transactional
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
        String userId = requestIssueDto.getUserId();
        try{
            boolean isExistUserId = userRepository.existsByUserId(userId);

            long refreshTokedValidationResult = redisUtil.getDataExpire(userId);

            if(!isExistUserId){
                throw new UsernameNotFoundException("해당 유저는 없습니다.");
            }

            if(refreshTokedValidationResult <= 0 || !tokenUtil.validateToken(redisUtil.getData(userId))){
                throw new CustomTokenException("RefreshToken not valid", "RefreshToken_Error");
            }

            //특정시간 이하면 리프레쉬 토큰을 다시 재발급
            if(refreshTokedValidationResult <= 3600){
                String refreshToken = tokenUtil.doGenerateToken(userId,TokenUtil.REFRESH_TOKEN_VALIDATION_SECOND);
                redisUtil.setDataExpire(userId, refreshToken,TokenUtil.REFRESH_TOKEN_VALIDATION_SECOND);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String accessToken = tokenUtil.doGenerateToken(userId,TokenUtil.TOKEN_VALIDATION_SECOND);

        return cookieUtil.createCookie(TokenUtil.ACCESS_TOKEN,accessToken);
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        UserEntity userEntity =userRepository.findByUserId(userId)
                .orElseThrow(()-> new UsernameNotFoundException("해당 유저는 없습니다."));
        log.info("가져온 userEntity : "+userEntity.getUserId()+" "+userEntity.getPassword());
        return new User(userEntity.getUserId(), userEntity.getPassword(), true, true, true, true,
                new ArrayList<>());
    }

}
