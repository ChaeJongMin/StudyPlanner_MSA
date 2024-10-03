package com.studyplaner.authservice.Auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studyplaner.authservice.Dto.RequestLoginDto;
import com.studyplaner.authservice.Entity.UserEntity;
import com.studyplaner.authservice.Repository.UserRepository;
import com.studyplaner.authservice.service.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class LoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final TokenUtil tokenUtil;
    private final CookieUtil cookieUtil;
    private final RedisUtil redisUtil;
    private final ResponseTokenUtil responseTokenUtil;
    private final UserServiceImpl userServiceImpl;

    String messageBody = null;
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("attemptAuthentication 작동");
        Authentication authentication;
        try {
            RequestLoginDto credentials = new ObjectMapper().readValue(request.getInputStream(), RequestLoginDto.class);
            log.info("credentials : {} , {}",credentials.getUserId(), credentials.getPassword());

            authentication = getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(credentials.getUserId(), credentials.getPassword(),new ArrayList<>()));
        } catch (IOException e) {
            e.fillInStackTrace();
            throw new RuntimeException(e);
        }

        log.info("authentication : {} , {}",authentication.getPrincipal(), authentication.getCredentials());

        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        User user = (User)authResult.getPrincipal();

        String userId = user.getUsername();
        log.info("액세스 토큰을 생성하겠습니다.");
        //토큰 생성
        String accessToken = tokenUtil.doGenerateToken(userId, TokenUtil.TOKEN_VALIDATION_SECOND);

        //리프레쉬 토큰 생성 및 업데이트
        long refreshTokenExpireFlag = redisUtil.getDataExpire(userId);

        log.info("리프레쉬ㅣ 토큰을 생성하겠습니다.");
        String refreshToken = tokenUtil.doGenerateToken(userId,TokenUtil.REFRESH_TOKEN_VALIDATION_SECOND);
        redisUtil.setDataExpire(userId, refreshToken,TokenUtil.REFRESH_TOKEN_VALIDATION_SECOND);

        //쿠키 설정
        Cookie cookie = cookieUtil.createCookie(TokenUtil.ACCESS_TOKEN ,accessToken);

        response.addCookie(cookie);

        String json= responseTokenUtil.sendResponse(response,HttpServletResponse.SC_OK,"AccessToken","Access token issued");

        log.info("로그인 응답값  : {}", json);

        response.getWriter().write(json);
        response.getWriter().flush();
    }
}
