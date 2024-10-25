package com.studyplaner.statisticservcie.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class StatisticTodoEntity { //타 서비스의 Todo 엔티티 데이터 일부를 가져와 별도의 엔티티로 생성
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="statistic_todo_id")
    private long id;

    private long userId;
    private String date;

    private int todayCount;
    private int yesterdayCount;

    public StatisticTodoEntity(long userId, String date){
        this.userId = userId;
        this.date = date;
        this.todayCount = 0;
        this.yesterdayCount=0;
    }

    public void update(){
        this.todayCount+=1;
    }

    public void init(String date){
        this.todayCount=0;
        this.yesterdayCount=0;
        if(!date.isEmpty()){
            this.date = date;
        }
    }
    public void updateDateAndCnt(String date,int count){
        this.date = date;
        this.yesterdayCount = this.todayCount;
        this.todayCount=count;
    }

}
