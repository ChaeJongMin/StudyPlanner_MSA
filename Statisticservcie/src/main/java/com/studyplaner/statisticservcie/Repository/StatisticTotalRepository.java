package com.studyplaner.statisticservcie.Repository;

import com.studyplaner.statisticservcie.Entity.StatisticTotalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatisticTotalRepository extends JpaRepository<StatisticTotalEntity, Long> {

    Optional<StatisticTotalEntity> findByUserId(long userId);

}
