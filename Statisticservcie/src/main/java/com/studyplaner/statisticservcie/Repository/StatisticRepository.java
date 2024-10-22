package com.studyplaner.statisticservcie.Repository;

import com.studyplaner.statisticservcie.Entity.StatisticEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface StatisticRepository extends JpaRepository<StatisticEntity, Long> {

    Optional<StatisticEntity> findByUserId(long userId);

    @Query("SELECT se FROM StatisticEntity se WHERE se.userId IN :userIds")
    List<StatisticEntity> findByUserIdIn(@Param("userIds") Set<Long> userIds);

}
