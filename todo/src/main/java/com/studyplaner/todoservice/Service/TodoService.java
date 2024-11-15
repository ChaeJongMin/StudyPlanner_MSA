package com.studyplaner.todoservice.Service;

import com.studyplaner.todoservice.Dto.*;

import java.time.LocalDate;
import java.util.List;

public interface TodoService {
    //저장
    public ResponseCommon save(RequestSaveTodoDto requestSaveTodoDto);
    //삭제
    public ResponseCommon delete(RequestDeleteTodo requestDeleteTodo);
    //수정
    public ResponseCommon update(RequestUpdateTodoDto requestUpdateTodoDto);

    //조회 (특정 달)
    public List<GetSimpleQueryDto> getListByDay(String userId, LocalDate date);

    public List<GetSimpleQueryDto> getListByMonth(String userId, String month);
}
