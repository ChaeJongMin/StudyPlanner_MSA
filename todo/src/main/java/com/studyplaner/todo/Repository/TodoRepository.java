package com.studyplaner.todo.Repository;

import com.studyplaner.todo.Entity.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity,Long> {
    //Todo 가져오기
    Optional<TodoEntity> findByUserId(long userId);
    //Todo 삭제
    void deleteByUserId(long userId);

}
