package com.studyplaner.todoservice.Controller;

import com.studyplaner.todoservice.Dto.*;
import com.studyplaner.todoservice.Service.TodoServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ToDoApi {

    private final TodoServiceImpl todoService;

    //할일 추가
    @PostMapping("/todo")
    public ResponseEntity<ResponseCommon> saveTodo(@RequestBody RequestSaveTodoDto requestSaveTodoDto){

        log.info("할일 추가를 하겠습니다.!!!");
        todoService.save(requestSaveTodoDto);

        final ResponseCommon responseCommon = ResponseCommon.builder()
                .code("save_todo")
                .message("해당 할일을 저장했습니다.")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseCommon);
    }
    //할일 탐색
    @GetMapping("/todo")
    public ResponseEntity<List<GetSimpleQueryDto>> getTodo(@RequestBody RequestGetMonth requestGetMonth){

        List<GetSimpleQueryDto> resultList = todoService.getListByMonth(requestGetMonth);
        return ResponseEntity.status(HttpStatus.OK).body(resultList);
    }
    //할일 삭제
    @DeleteMapping("/todo")
    public ResponseEntity<ResponseCommon> deleteTodo(@RequestParam long id){
        todoService.delete(id);

        final ResponseCommon responseCommon = ResponseCommon.builder()
                .code("delete_todo")
                .message("해당 할일을 삭제했습니다.")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseCommon);
    }
    //할일 수정
    @PutMapping("/todo")
    public ResponseEntity<ResponseCommon> updateTodo(@RequestBody RequestUpdateTodoDto requestUpdateTodoDto){
        todoService.update(requestUpdateTodoDto);

        final ResponseCommon responseCommon = ResponseCommon.builder()
                .code("update_todo")
                .message("해당 할일을 수정했습니다.")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseCommon);
    }

    @PutMapping("/todo/complete")
    public ResponseEntity<ResponseCommon> setCompleteTodo(@RequestBody RequestUpdateTodoDto requestUpdateTodoDto){
        todoService.completeTodo(requestUpdateTodoDto);

        final ResponseCommon responseCommon = ResponseCommon.builder()
                .code("complete_todo")
                .message("해당 할일을 완료했습니다.")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseCommon);
    }
}
