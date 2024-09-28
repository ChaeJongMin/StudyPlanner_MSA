package com.studyplaner.authservice.Auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studyplaner.authservice.service.ResponseTokenUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ResponseTokenUtil responseTokenUtil;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ObjectMapper objectMapper = new ObjectMapper();

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String errorMessage;

        if (authException instanceof BadCredentialsException) {
            errorMessage = "Invalid username or password.";
        } else {
            errorMessage = "Authentication failed.";
        }

        String json= responseTokenUtil.sendResponse(response,HttpServletResponse.SC_UNAUTHORIZED,"AccessToken","Access token issued");
        response.getWriter().write(json);
        response.getWriter().flush();


    }
}
