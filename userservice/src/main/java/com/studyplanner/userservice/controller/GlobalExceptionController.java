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

        log.error("CustomException : {}, {}, {}",status,code,message);

        return ResponseEntity.status(status).body(new ResponseException(status.value(),code,message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> responseDefaultException(Exception e){
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String code = "Internal_Server_Error";
        String message = e.getMessage();

        log.error("500 Server Error: {}, {}, {}", status, code, message);

        return ResponseEntity.status(status).body(new ResponseException(status.value(),code,message));
    }
}
