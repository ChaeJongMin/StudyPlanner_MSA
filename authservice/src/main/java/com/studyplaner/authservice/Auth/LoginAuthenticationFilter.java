package com.studyplaner.authservice.Auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studyplaner.authservice.Dto.RequestLoginDto;
import com.studyplaner.authservice.service.CookieUtil;
import com.studyplaner.authservice.service.RedisUtil;
import com.studyplaner.authservice.service.ResponseTokenUtil;
import com.studyplaner.authservice.service.TokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class LoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final TokenUtil tokenUtil;
    private final CookieUtil cookieUtil;
    private final RedisUtil redisUtil;
    private final ResponseTokenUtil responseTokenUtil;

    String messageBody = null;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        Authentication authentication;
        try {
            RequestLoginDto credentials = new ObjectMapper().readValue(request.getInputStream(), RequestLoginDto.class);
            authentication = getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(credentials.getUserId(),credentials.getPassword()));
        } catch (IOException e) {
            e.fillInStackTrace();
            throw new RuntimeException(e);
        }

        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        User user = (User)authResult.getPrincipal();

        String userId = user.getUsername();

        //토큰 생성
        String accessToken = tokenUtil.doGenerateToken(userId, TokenUtil.TOKEN_VALIDATION_SECOND);

        //리프레쉬 토큰 생성 및 업데이트
        long refreshTokenExpireFlag = redisUtil.getDataExpire(userId);

        if(refreshTokenExpireFlag <= 0){
            String refreshToken = tokenUtil.doGenerateToken(userId,TokenUtil.REFRESH_TOKEN_VALIDATION_SECOND);
            redisUtil.setDataExpire(userId, refreshToken,TokenUtil.REFRESH_TOKEN_VALIDATION_SECOND);
        }

        //쿠키 설정
        Cookie cookie = cookieUtil.createCookie(TokenUtil.ACCESS_TOKEN ,accessToken);

        response.addCookie(cookie);

        String json= responseTokenUtil.sendResponse(response,HttpServletResponse.SC_OK,"AccessToken","Access token issued");
        response.getWriter().write(json);
        response.getWriter().flush();
    }
}
