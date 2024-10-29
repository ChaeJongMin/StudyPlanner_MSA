package com.studyplaner.statisticservcie.Entity;

import com.studyplaner.statisticservcie.Dto.ResponseMultiDto;
import com.studyplaner.statisticservcie.Dto.ResponseSingleDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
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
    private int successCnt = 0;

    // 일간 통계
    private int dailyTotalTodoCount;
    private int dailySuccessCnt = 0;

    // 주간 통계
    private int weeklyTotalTodoCount;
    private int weeklySuccessCnt = 0;

    // 월간 통계
    private int monthlyTotalTodoCount;
    private int monthlySuccessCnt = 0;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "statistic_todo_id",referencedColumnName = "id")
    private StatisticTodoEntity statisticTodo;

    public StatisticEntity(long userId,StatisticTodoEntity todoEntity){
        this.userId = userId;
        this.statisticTodo = todoEntity;
        this.totalTodoCount = this.dailyTotalTodoCount =this.weeklyTotalTodoCount =this.monthlyTotalTodoCount =1;

    }

    public ResponseSingleDto toResponseSingleDto() {
        return ResponseSingleDto.builder()
                .successCnt(this.successCnt)
                .totalCnt(this.totalTodoCount)
                .failCnt(this.totalTodoCount-this.successCnt)
                .totalSuccessRate(calculateTotalSuccessRate("total"))
                .build();
    }

    public ResponseMultiDto toResponseMultiDto(){
        return ResponseMultiDto.builder()
                .dailySuccessRate(calculateTotalSuccessRate("daily"))
                .weeklySuccessRate(calculateTotalSuccessRate("week"))
                .monthlySuccessRate(calculateTotalSuccessRate("month"))
                .totalSuccessRate(calculateTotalSuccessRate("total"))
                .build();
    }

    public double calculateTotalSuccessRate(String type) {
        double rate = 0.0;
        switch (type){
            case "total" :
                rate = this.totalTodoCount == 0 ? 0 : ((double) this.successCnt / this.totalTodoCount) * 100;
                break;
            case "daily" :
                rate = this.dailyTotalTodoCount == 0 ? 0 : ((double) this.dailySuccessCnt / this.dailyTotalTodoCount) * 100;
                break;
            case "week" :
                rate = this.weeklyTotalTodoCount == 0 ? 0 : ((double) this.weeklySuccessCnt / this.weeklyTotalTodoCount) * 100;
                break;
            case "month" :
                rate = this.monthlyTotalTodoCount == 0 ? 0 : ((double) this.monthlySuccessCnt / this.monthlyTotalTodoCount) * 100;
                break;
            default:
                rate = 0.0;
        }

        return rate;
    }

    public void updateSuccessTodoCnt(int count){
        this.successCnt +=count;
        this.dailySuccessCnt +=count;
        this.weeklySuccessCnt += count;
        this.monthlySuccessCnt += count;
    }

    public void updateSuccessTodoCntToChangeCondition(int totalCount, String change) {
        this.successCnt += totalCount;

        switch (change) {
            case "weekmonth":
                this.weeklyTotalTodoCount = 0;
                this.weeklySuccessCnt = 0;
                this.monthlyTotalTodoCount = 0;
                this.monthlySuccessCnt = 0;
                break;

            case "month":
                this.monthlyTotalTodoCount = 0;
                this.monthlySuccessCnt = 0;
                break;

            case "weekly":
                this.weeklyTotalTodoCount = 0;
                this.weeklySuccessCnt = 0;
                break;
        }

        this.dailySuccessCnt = 0;
        this.dailyTotalTodoCount = 0;
    }

    public void updateTotalCnt(){
        this.totalTodoCount+=1;
    }
}
