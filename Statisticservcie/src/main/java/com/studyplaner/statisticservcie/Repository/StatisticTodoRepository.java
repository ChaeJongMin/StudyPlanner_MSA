package com.studyplaner.statisticservcie.Repository;

import com.studyplaner.statisticservcie.Entity.StatisticTodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StatisticTodoRepository extends JpaRepository<StatisticTodoEntity,Long> {
    Optional<StatisticTodoEntity> findByUserId(long userId);

}
