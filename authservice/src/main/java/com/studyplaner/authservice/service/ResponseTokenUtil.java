package com.studyplaner.authservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@RequiredArgsConstructor
@Slf4j
public class ResponseTokenUtil {

    private final ObjectMapper objectMapper;

    public String sendResponse(HttpServletResponse response, int status, String code, String message) throws IOException {
        log.info("응답값의 status : "+status);

        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(status); // 상태 코드 설정
        response.setContentType("application/json"); // 응답 타입 설정

        // JSON 응답 생성
        Map<String, String> jsonResponse = new HashMap<>();
        jsonResponse.put("message", message );
        jsonResponse.put("code", code);

        // JSON으로 변환
        return objectMapper.writeValueAsString(jsonResponse);
    }

    public String sendResponse(int status, String code, String message) throws IOException {

        // JSON 응답 생성
        Map<String, String> jsonResponse = new HashMap<>();
        jsonResponse.put("status", String.valueOf(status));
        jsonResponse.put("message", message );
        jsonResponse.put("code", code);

        // JSON으로 변환
        return objectMapper.writeValueAsString(jsonResponse);
    }
}
