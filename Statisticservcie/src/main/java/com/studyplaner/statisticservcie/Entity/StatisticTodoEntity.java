package com.studyplaner.statisticservcie.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class StatisticTodoEntity { //타 서비스의 Todo 엔티티 데이터 일부를 가져와 별도의 엔티티로 생성
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //나중에 처리하자
    private long id;

    private long userId;

    private String date;

    private int count;

    public StatisticTodoEntity(long userId, String date){
        this.userId = userId;
        this.date = date;
        count = 0;
    }
    public void update(){
        this.count+=1;
    }

}
