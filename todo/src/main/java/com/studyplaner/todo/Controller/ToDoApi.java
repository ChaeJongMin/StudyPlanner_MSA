package com.studyplaner.todo.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ToDoApi {

    //할일 추가
    @PostMapping("/todo")
    public ResponseEntity<?> saveTodo(){

    }
    //할일 탐색
    @GetMapping("/todo")
    public ResponseEntity<?> getTodo(){

    }
    //할일 삭제
    @DeleteMapping("/todo")
    public ResponseEntity<String> deleteTodo(){

    }
    //할일 수정
    @PutMapping("/todo")
    public ResponseEntity<String> updateTodo(){

    }
}
