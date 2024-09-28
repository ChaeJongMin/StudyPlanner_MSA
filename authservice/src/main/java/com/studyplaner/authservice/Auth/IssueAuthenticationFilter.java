package com.studyplaner.authservice.Auth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studyplaner.authservice.Error.CustomTokenException;
import com.studyplaner.authservice.service.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class IssueAuthenticationFilter extends OncePerRequestFilter {

    private final UserServiceImpl userServiceImpl;
    private final TokenUtil tokenUtil;
    private final RedisUtil redisUtil;
    private final ResponseTokenUtil responseTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {


        //올바른 유저인지 재확인
        if ("POST".equalsIgnoreCase(request.getMethod()) && request.getContentType().contains("application/json")) {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> jsonMap = objectMapper.readValue(request.getInputStream(), new TypeReference<Map<String, Object>>() {});
            String userId = (String) jsonMap.get("userId");

            // SecurityContextHolder에서 인증 정보 가져오기
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

//            // 인증 여부 확인
//            if (authentication != null && authentication.isAuthenticated()) {
//                if (!userId.equals(authentication.getName())) {
//                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User ID does not match");
//                    return;
//                }
//            } else {
//                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User authentication failed");
//                return;
//            }
            UserDetails userDetails = userServiceImpl.loadUserByUsername(userId);
            try{
                long refreshTokedValidationResult = redisUtil.getDataExpire(userId);
                if(refreshTokedValidationResult <= 0 || !tokenUtil.validateToken(redisUtil.getData(userId),userDetails)){
                    throw new CustomTokenException("RefreshToken not valid", "RefreshToken_Error");
                }

                //특정시간 이하면 리프레쉬 토큰을 다시 재발급
                if(refreshTokedValidationResult <= 3600){
                    String refreshToken = tokenUtil.doGenerateToken(userId,TokenUtil.REFRESH_TOKEN_VALIDATION_SECOND);
                    redisUtil.setDataExpire(userId, refreshToken,TokenUtil.REFRESH_TOKEN_VALIDATION_SECOND);
                }

            } catch (CustomTokenException e){
                String json= responseTokenUtil.sendResponse(response,HttpServletResponse.SC_UNAUTHORIZED,"AccessToken","Access token issued");
                response.getWriter().write(json);
                response.getWriter().flush();
            }
        }

        // 필터 체인의 다음 필터 호출
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return requestURI.equals("/login") || requestURI.equals("/logout");
    }
}
