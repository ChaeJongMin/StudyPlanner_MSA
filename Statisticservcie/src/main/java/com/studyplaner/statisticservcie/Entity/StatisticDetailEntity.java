package com.studyplaner.statisticservcie.Entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class StatisticDetailEntity { //타 서비스의 Todo 엔티티 데이터 일부를 가져와 별도의 엔티티로 생성
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long userId;
    private String date; //fomart yyyy-mm-dd-weekOfMonth (weekOfMonth는 한국식 기준으로 해당 월의 몇번 쨰 주인지 나타냄)
    private int createCnt;
    private int successCnt;


    @Builder
    public  StatisticDetailEntity(long userId, String date, int createCnt){
        this.userId = userId;
        this.date = date;
        this.createCnt = createCnt;
        this.successCnt = 0;
    }

    public void updateCreateCount(int count){
        this.createCnt += count;
        if(createCnt<0)
            this.createCnt = 0;
    }

    public void updateSuccessCount(int count){
        this.successCnt += count;
        if(successCnt<0)
            this.successCnt = 0;
    }
}
