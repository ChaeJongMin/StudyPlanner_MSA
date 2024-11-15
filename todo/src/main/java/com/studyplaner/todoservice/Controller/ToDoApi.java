package com.studyplaner.todoservice.Controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.studyplaner.todoservice.Dto.*;
import com.studyplaner.todoservice.Service.TodoServiceImpl;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
    @RefreshScope
public class ToDoApi {

    private final TodoServiceImpl todoService;

    @Value("${service.name}") // yml 파일에서 service.name 값을 가져옴
    private String serviceName;

    @GetMapping("/checkRefresh")
    public String getServiceName (){
        return serviceName;
    }
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
    @GetMapping("/todo/{userId}")
    public ResponseEntity<List<GetSimpleQueryDto>> getTodoByDay(@PathVariable String userId,
                                                                @RequestParam("date") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MMM-dd", timezone = "Asia/Seoul") LocalDate date){

        List<GetSimpleQueryDto> resultList = todoService.getListByDay(userId, date);
        return ResponseEntity.status(HttpStatus.OK).body(resultList);
    }

    @GetMapping("/todo/month/{userId}")
    public ResponseEntity<List<GetSimpleQueryDto>> getTodoByMonth(@PathVariable String userId, @RequestParam String month){

        List<GetSimpleQueryDto> resultList = todoService.getListByMonth(userId, month);
        return ResponseEntity.status(HttpStatus.OK).body(resultList);
    }

    //할일 삭제
    @DeleteMapping("/todo")
    public ResponseEntity<ResponseCommon> deleteTodo(@RequestBody RequestDeleteTodo requestDeleteTodo){
        todoService.delete(requestDeleteTodo);

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
}
