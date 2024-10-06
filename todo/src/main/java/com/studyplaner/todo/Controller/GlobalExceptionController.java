package com.studyplaner.todo.Controller;

import com.studyplaner.todo.Dto.ResponseException;
import com.studyplaner.todo.Error.NotFoundUserOrTodoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

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
