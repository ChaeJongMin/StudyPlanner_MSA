package com.studyplaner.todoservice.Controller;

import com.studyplaner.todoservice.Dto.ResponseException;
import com.studyplaner.todoservice.Error.NotFoundUserOrTodoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleServerException(Exception ex) {
        final ResponseException responseException = ResponseException.builder()
                .code("Server_Error")
                .message("서버 내부에 오류가 발생했습니다.")
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(responseException);
    }

    @ExceptionHandler(NotFoundUserOrTodoException.class)
    public ResponseEntity<?> handleNoSuchElementException(NotFoundUserOrTodoException ex) {
        final ResponseException responseException = ResponseException.builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .build();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(responseException);
    }


}
