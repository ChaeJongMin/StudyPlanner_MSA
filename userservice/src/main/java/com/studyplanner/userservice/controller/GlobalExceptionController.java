package com.studyplanner.userservice.controller;

import com.studyplanner.userservice.Exception.CustomException;
import com.studyplanner.userservice.dto.response.ResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionController {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> responseCustomException(CustomException e){
        HttpStatus status = e.getStatus();
        String code = e.getCode();
        String message = e.getMessage();

        ResponseException responseException = ResponseException.builder()
                .code(e.getCode())
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(status).body(responseException);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> responseDefaultException(Exception e){
        ResponseException responseException = ResponseException.builder()
                .code("Internal_Server_Error")
                .message("서버 내부에 오류가 발생했습니다.")
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseException);
    }
}
