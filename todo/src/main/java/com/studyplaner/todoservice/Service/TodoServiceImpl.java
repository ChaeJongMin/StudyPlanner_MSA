package com.studyplaner.todoservice.Service;

import com.studyplaner.todoservice.Dto.*;
import com.studyplaner.todoservice.Error.NotFoundUserOrTodoException;
import com.studyplaner.todoservice.Entity.TodoEntity;
import com.studyplaner.todoservice.Entity.UserEntity;
import com.studyplaner.todoservice.MessageQueue.KafkaProducer;
import com.studyplaner.todoservice.MessageQueue.KafkaSendDeleteDto;
import com.studyplaner.todoservice.MessageQueue.KafkaSendUpdateDto;
import com.studyplaner.todoservice.MessageQueue.KakfaSendDto;
import com.studyplaner.todoservice.Repository.TodoRepository;
import com.studyplaner.todoservice.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


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
    private final KafkaProducer kafkaProducer;

    @Transactional
    @Override
    public ResponseCommon save(RequestSaveTodoDto requestSaveTodoDto) {
        UserEntity userEntity = userRepository.findByUserId(requestSaveTodoDto.getUserId())
                .orElseThrow(() -> new NotFoundUserOrTodoException("Not_Found_User", "해당 유저는 없습니다."));

        List<TodoEntity> todoEntities = requestSaveTodoDto.getContextList().stream()
                .map(context -> requestSaveTodoDto.toTodoEntity(userEntity.getId(), context))
                .toList();

        todoRepository.saveAll(todoEntities);

        kafkaProducer.sendTodoCreate(KakfaSendDto.builder()
                .count(todoEntities.size())
                .date(requestSaveTodoDto.getDate().toString())
                .userId(userEntity.getId())
                .build());

        return ResponseCommon.builder()
                .code(SAVE_CODE)
                .message(SAVE_MESSAGE)
                .build();
    }

    @Transactional
    @Override
    public ResponseCommon delete(RequestDeleteTodo requestDeleteTodo) {
        List<TodoEntity> todoEntities = todoRepository.findByUserIdAndDateAndIdIn(
                requestDeleteTodo.getUserId(), requestDeleteTodo.getDate(), requestDeleteTodo.getTodoIdList());

        int successCnt = (int) todoEntities.stream().filter(TodoEntity::isComplete).count();

        kafkaProducer.sendTodoDelete(KafkaSendDeleteDto.builder()
                .successCnt(successCnt)
                .totalCnt(todoEntities.size())
                .date(todoEntities.isEmpty() ? "" : todoEntities.get(0).getDate().toString())
                .userId(todoEntities.isEmpty() ? 0 : todoEntities.get(0).getUserId())
                .build());

        todoRepository.deleteAll(todoEntities);

        return ResponseCommon.builder()
                .code(DELETE_CODE)
                .message(DELETE_MESSAGE)
                .build();
    }

    @Transactional
    @Override
    public ResponseCommon update(RequestUpdateTodoDto requestUpdateTodoDto) {
        List<TodoEntity> todoEntities = todoRepository.findByUserIdAndDateAndIdIn(
                requestUpdateTodoDto.getUserId(), requestUpdateTodoDto.getDate(),
                requestUpdateTodoDto.getDetailList().stream().map(RequestUpdateDetailTodoDto::getTodoId).toList());

        AtomicInteger successCnt = new AtomicInteger();

        for (TodoEntity todoEntity : todoEntities) {
            requestUpdateTodoDto.getDetailList().stream()
                    .filter(dto -> dto.getTodoId() == todoEntity.getId())
                    .findFirst()
                    .ifPresent(detailDto -> {
                        if (!"none".equals(detailDto.getContext())) {
                            todoEntity.updateContext(detailDto.getContext());
                        }
                        if (todoEntity.isComplete() != detailDto.isComplete()) {
                            if (detailDto.isComplete()) {
                                successCnt.getAndIncrement();
                            }
                            todoEntity.updateIsComplete(detailDto.isComplete());
                        }
                    });
        }

        kafkaProducer.sendTodoSuccess(KafkaSendUpdateDto.builder()
                .date(requestUpdateTodoDto.getDate())
                .userId(requestUpdateTodoDto.getUserId())
                .successCnt(successCnt.get())
                .build());

        return ResponseCommon.builder()
                .code(UPDATE_CODE)
                .message(UPDATE_MESSAGE)
                .build();
    }


    @Override
    public List<GetSimpleQueryDto> getListByDay(String userId, LocalDate date) {
        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundUserOrTodoException("Not_Found_User", "해당 유저는 없습니다."));

        return todoRepository.findByDay(date, userEntity.getId());
    }

    @Override
    public List<GetSimpleQueryDto> getListByMonth(String userId, String month) {
        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundUserOrTodoException("Not_Found_User", "해당 유저는 없습니다."));

        return todoRepository.findByMonth(month, userEntity.getId());
    }

}
