package com.studyplaner.statisticservcie.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class StatisticTotalEntity {
    //아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    //유저 아이디
    private long userId;
    //전체 수
    private int createCount;
    private int successCnt;

    @Builder
    public StatisticTotalEntity(long userId,int createCount){
        this.userId = userId;
        this.createCount = createCount;
        this.successCnt = 0;
    }

    public void updateCreateCnt(int count){
        this.createCount += count;
        if(createCount<0)
            this.createCount = 0;
    }

    public void updateSuccessCnt(int count){
        this.successCnt += count;
        if(successCnt<0)
            this.successCnt = 0;
    }

}
