package com.studyplaner.todo.Service;

import com.studyplaner.todo.Dto.RequestSaveTodoDto;
import com.studyplaner.todo.Error.NotFoundUserOrTodoException;
import com.studyplaner.todo.Dto.RequestUpdateTodoDto;
import com.studyplaner.todo.Dto.ResponseCommon;
import com.studyplaner.todo.Entity.TodoEntity;
import com.studyplaner.todo.Entity.UserEntity;
import com.studyplaner.todo.Repository.TodoRepository;
import com.studyplaner.todo.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;


@RequiredArgsConstructor
@Service
@Slf4j
public class TodoServiceImpl implements TodoService{

    final static private String SAVE_CODE = "Success save";
    final static private String DELETE_CODE = "Success Delete";
    final static private String UPDATE_CODE = "Success Update";
    final static private String COMPLETE_CODE = "Clear Todo";

    final static private String SAVE_MESSAGE = "Todo 정보가 정상적으로 저장되었습니다.";
    final static private String DELETE_MESSAGE = "Todo 정보가 정상적으로 삭제되었습니다.";
    final static private String UPDATE_MESSAGE = "Todo 정보가 정상적으로 수정되었습니다.";
    final static private String COMPLETE_MESSAGE = "해당 할일을 정상적으로 수행했습니다.";

    private final UserRepository userRepository;
    private final TodoRepository todoRepository;

    @Transactional
    @Override
    public ResponseCommon save(RequestSaveTodoDto requestSaveTodoDto) {

        //유저아이디로부터 LongId 추출
        UserEntity userEntity =userRepository.findByUserId(requestSaveTodoDto.getUserId())
                .orElseThrow(()-> new NotFoundUserOrTodoException("Not_Found_User","해당 유저는 없습니다."));
        //Todo 객체 생성
        Long id = userEntity.getId();
        TodoEntity todoEntity = requestSaveTodoDto.toTodoEntity(id);

        //Todo 객체 저장
        todoRepository.save(todoEntity);

        return ResponseCommon.builder()
                .code(SAVE_CODE)
                .message(SAVE_MESSAGE)
                .build();
    }

    @Transactional
    @Override
    public ResponseCommon delete(long id) {

        todoRepository.deleteByUserId(id);

        return ResponseCommon.builder()
                .code(DELETE_CODE)
                .message(DELETE_MESSAGE)
                .build();
    }

    @Transactional
    @Override
    public ResponseCommon update(RequestUpdateTodoDto requestUpdateTodoDto) {

        //유저아이디로부터 LongId 추출
        UserEntity userEntity =userRepository.findByUserId(requestUpdateTodoDto.getUserId())
                .orElseThrow(()-> new NotFoundUserOrTodoException("Not_Found_User","해당 유저는 없습니다."));

        TodoEntity todoEntity = todoRepository.findByUserId(userEntity.getId())
                .orElseThrow(()-> new NotFoundUserOrTodoException("Not_Found_Todo","해당 할일을 찾을 수 없습니다.."));

        todoEntity.update(requestUpdateTodoDto.getContext());

        return ResponseCommon.builder()
                .code(UPDATE_CODE)
                .message(UPDATE_MESSAGE)
                .build();
    }

    @Override
    public ResponseCommon completeTodo(RequestUpdateTodoDto requestUpdateTodoDto) {
        UserEntity userEntity =userRepository.findByUserId(requestUpdateTodoDto.getUserId())
                .orElseThrow(()-> new NotFoundUserOrTodoException("Not_Found_User","해당 유저는 없습니다."));

        TodoEntity todoEntity = todoRepository.findByUserId(userEntity.getId())
                .orElseThrow(()-> new NotFoundUserOrTodoException("Not_Found_Todo","해당 할일을 찾을 수 없습니다.."));

        todoEntity.complete();

        return ResponseCommon.builder()
                .code(COMPLETE_CODE)
                .message(COMPLETE_MESSAGE)
                .build();
    }

    @Override
    public List<TodoEntity> getListByMonth(int month) {
        LocalDate newDate = LocalDate.of(2021, 02,1);
        int lengthOfMon = newDate.lengthOfMonth();
        return null;
    }


}
