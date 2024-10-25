package com.studyplaner.statisticservcie.Entity;

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

    public void updateSuccessTodoCntToChangeCondition(int totalCount,int todayCount,String change){
        this.successCnt +=totalCount;

        //월, 주 , 일 일떄
        if(change.equals("month")){
            this.monthlySuccessCnt = todayCount;
            this.monthlyTotalTodoCount = todayCount;
        }  else if(change.equals("week")){
            this.weeklySuccessCnt = todayCount;
            this.weeklyTotalTodoCount = todayCount;
        } else if(){
            this.monthlySuccessCnt = todayCount;
            this.monthlyTotalTodoCount = todayCount;
            this.weeklySuccessCnt = todayCount;
            this.weeklyTotalTodoCount = todayCount;
        }
        this.dailySuccessCnt = 0;
        this.dailyTotalTodoCount = 0;

        this.dailySuccessCnt +=count;
        this.weeklySuccessCnt += count;
    }

    public void updateTotalCnt(){
        this.totalTodoCount+=1;
    }
}
