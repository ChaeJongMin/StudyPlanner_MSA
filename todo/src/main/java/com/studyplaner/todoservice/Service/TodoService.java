package com.studyplaner.todoservice.Service;

import com.studyplaner.todoservice.Dto.*;

import java.util.List;

public interface TodoService {
    //저장
    public ResponseCommon save(RequestSaveTodoDto requestSaveTodoDto);
    //삭제
    public ResponseCommon delete(long id);
    //수정
    public ResponseCommon update(RequestUpdateTodoDto requestUpdateTodoDto);
    //할일 완료
    public ResponseCommon completeTodo(RequestUpdateTodoDto requestUpdateTodoDto);
    //조회 (특정 달)
    public List<GetSimpleQueryDto> getListByMonth(RequestGetMonth requestGetMonth);
}
