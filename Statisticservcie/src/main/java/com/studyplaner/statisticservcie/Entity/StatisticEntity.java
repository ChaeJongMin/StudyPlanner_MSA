package com.studyplaner.statisticservcie.Entity;

import com.studyplaner.statisticservcie.Dto.ResponseSingleDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
@Entity
public class StatisticEntity {
    //아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    //유저 아이디
    private long userId;
    //전체 수
    private int totalTodoCount;
    //성공 수
    private int successCnt;

    // 일간 통계
    private int dailyTotalTodoCount;
    private int dailySuccessCnt;

    // 주간 통계
    private int weeklyTotalTodoCount;
    private int weeklySuccessCnt;

    // 월간 통계
    private int monthlyTotalTodoCount;
    private int monthlySuccessCnt;

    public ResponseSingleDto toResponseSingleDto() {
        return ResponseSingleDto.builder()
                .successCnt(this.successCnt)
                .totalCnt(this.totalTodoCount)
                .totalSuccessRate(calculateTotalSuccessRate())
                .build();
    }

    private double calculateTotalSuccessRate() {
        return totalTodoCount == 0 ? 0 : ((double) successCnt / totalTodoCount) * 100;
    }

    public void updateSuccessTodoCnt(int count){
        this.successCnt +=count;
        this.dailySuccessCnt +=count;
        this.weeklySuccessCnt += count;
        this.monthlySuccessCnt += count;
    }
}
