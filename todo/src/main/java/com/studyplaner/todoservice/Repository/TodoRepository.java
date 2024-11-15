package com.studyplaner.todoservice.Repository;

import com.studyplaner.todoservice.Dto.GetSimpleQueryDto;
import com.studyplaner.todoservice.Entity.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, Long> {
    //Todo 가져오기
    Optional<TodoEntity> findByUserId(long id);

    //Todo 삭제
    void deleteByUserId(long userId);


    @Query(value = "SELECT new com.studyplaner.todoservice.Dto.GetSimpleQueryDto(t.id, t.context, t.isComplete, DAY(t.date)) " +
            "FROM TodoEntity t " +
            "WHERE t.date = :wantDate AND t.userId = :userId " +
            "GROUP BY t.id, t.date " +
            "ORDER BY t.id")
    List<GetSimpleQueryDto> findByDay(@Param("wantDate") LocalDate wantDate, @Param("userId") long userId);

    @Query(value = "SELECT new com.studyplaner.todoservice.Dto.GetSimpleQueryDto(t.id, t.context, t.isComplete, DAY(t.date)) " +
            "FROM TodoEntity t " +
            "WHERE DATE_FORMAT(t.date,'%Y-%m') = :month AND t.userId = :userId " +
            "ORDER BY t.date, t.id")
    List<GetSimpleQueryDto> findByMonth(@Param("month") String month, @Param("userId") long userId);


    @Query("SELECT e FROM TodoEntity e " +
            "WHERE e.userId = :userId " +
            "AND DATE_FORMAT(e.date,'%Y-%m-%d') = :date " +
            "AND e.id IN :toids")
    List<TodoEntity> findByUserIdAndDateAndIdIn(@Param("userId") long userId,
                                                @Param("date") String date,
                                                @Param("toids") List<Long> toids);


}
