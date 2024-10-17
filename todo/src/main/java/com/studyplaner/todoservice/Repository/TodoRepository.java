package com.studyplaner.todoservice.Repository;

import com.studyplaner.todoservice.Dto.GetSimpleQueryDto;
import com.studyplaner.todoservice.Entity.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, Long> {
    //Todo 가져오기
    Optional<TodoEntity> findByUserId(long userId);

    //Todo 삭제
    void deleteByUserId(long userId);


        @Query(value = "SELECT new com.studyplaner.todoservice.Dto.GetSimpleQueryDto(t.id, t.context, t.isComplete, DAY(t.createdDate)) " +
            "FROM TodoEntity t " +
            "WHERE DATE_FORMAT(t.createdDate, '%Y-%m') = :wantDate AND t.userId = :userId " +
            "ORDER BY t.createdDate")
    List<GetSimpleQueryDto> findByDate(@Param("wantDate") String wantDate, @Param("userId") long userId);

}
