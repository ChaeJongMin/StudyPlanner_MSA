package com.studyplaner.statisticservcie.Repository;

import com.studyplaner.statisticservcie.Dto.Sql.ThisTotalDto;
import com.studyplaner.statisticservcie.Entity.StatisticDetailEntity;
import org.apache.kafka.common.quota.ClientQuotaAlteration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StatisticDetailRepository extends JpaRepository<StatisticDetailEntity,Long> {
    Optional<StatisticDetailEntity> findByUserIdAndDate(long userId,String date);

    // 이번 달 합계 조회
    @Query("SELECT new com.studyplaner.statisticservcie.Dto.Sql.ThisTotalDto(s.userId, SUM(s.createCnt), SUM(s.successCnt))" +
            "FROM StatisticDetailEntity s " +
            "WHERE s.userId = :userId AND s.date LIKE CONCAT(:yearMonth, '%') " +
            "GROUP BY s.userId")
    ThisTotalDto findMonthlySummary(@Param("userId") long userId,
                                    @Param("yearMonth") String yearMonth);

    // 이번 주 합계 조회
    @Query("SELECT new com.studyplaner.statisticservcie.Dto.Sql.ThisTotalDto(s.userId, SUM(s.createCnt), SUM(s.successCnt)) " +
            "FROM StatisticDetailEntity s " +
            "WHERE s.userId = :userId " +
            "AND s.date LIKE CONCAT(:yearMonth, '-%', '-', :weekOfMonth) " +
            "GROUP BY s.userId")
    ThisTotalDto findWeeklySummary(@Param("userId") long userId,
                                   @Param("yearMonth") String yearMonth,
                                   @Param("weekOfMonth") String weekOfMonth);
}
