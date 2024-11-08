package com.studyplaner.statisticservcie.Controller;

import com.studyplaner.statisticservcie.Dto.ResponseException;
import com.studyplaner.statisticservcie.Error.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionController {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> responseCustomException(CustomException e){
        String code = e.getCode();
        String message = e.getMessage();

        ResponseException responseException = ResponseException.builder()
                .code(e.getCode())
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(400).body(responseException);
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
