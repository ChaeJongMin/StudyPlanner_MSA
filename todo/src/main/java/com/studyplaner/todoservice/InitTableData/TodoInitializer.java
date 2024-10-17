package com.studyplaner.todoservice.InitTableData;

import com.studyplaner.todoservice.Entity.TodoEntity;
import com.studyplaner.todoservice.Repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TodoInitializer implements ApplicationRunner  {

    final private TodoRepository todoRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        TodoEntity todoEntity1 = new TodoEntity("테스트1",1);
        TodoEntity todoEntity2 = new TodoEntity("테스트3",1);
        TodoEntity todoEntity3 = new TodoEntity("테스트2",1);

        todoRepository.save(todoEntity1);
        todoRepository.save(todoEntity2);
        todoRepository.save(todoEntity3);
    }
}
