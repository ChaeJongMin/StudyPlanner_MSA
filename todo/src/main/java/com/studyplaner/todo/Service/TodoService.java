package com.studyplaner.todo.Service;

import com.studyplaner.todo.Dto.RequestSaveTodoDto;
import com.studyplaner.todo.Dto.RequestUpdateTodoDto;
import com.studyplaner.todo.Dto.ResponseCommon;
import com.studyplaner.todo.Entity.TodoEntity;

import java.util.List;
import java.util.Optional;

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
    public List<TodoEntity> getListByMonth(int month);


}
