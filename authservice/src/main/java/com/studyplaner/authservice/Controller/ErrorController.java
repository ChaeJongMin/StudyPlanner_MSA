package com.studyplaner.authservice.Controller;

import com.studyplaner.authservice.Dto.ErrorResponse;
import com.studyplaner.authservice.Error.CustomTokenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorController {

    // 서버 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleServerException(Exception ex) {
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .code("Server Error")
                .message("서버 내부에 오류가 발생했습니다.")
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }

    // 유저 존재 X 예외 처리
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UsernameNotFoundException ex) {

        final ErrorResponse errorResponse = ErrorResponse.builder()
                .code("UserNotFound")
                .message("해당 유저는 존재하지 않습니다.")
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    // CustomTokenException 예외 처리
    @ExceptionHandler(CustomTokenException.class)
    public ResponseEntity<?> handleCustomTokenException(CustomTokenException ex) {
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ex.getErrorCode())
                .message(ex.getMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(errorResponse);
    }
}
