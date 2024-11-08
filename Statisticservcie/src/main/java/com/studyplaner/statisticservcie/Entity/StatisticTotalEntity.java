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
    public StatisticTotalEntity(long userId){
        this.userId = userId;
        this.createCount = 1;
        this.successCnt = 0;
    }

    public void updateCreateCnt(String type){
        this.createCount += (type.equals("Add") ? 1 : -1);
    }

    public void updateSuccessCnt(String type){
        this.successCnt += (type.equals("Add") ? 1 : -1);
    }

}
