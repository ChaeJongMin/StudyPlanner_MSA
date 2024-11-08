package com.studyplaner.statisticservcie.Dto.Sql;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ThisTotalDto {

    private long userId;
    private Long total;
    private Long success;

    public ThisTotalDto(long userId, Long total, Long success){
        this.userId = userId;
        this.total = total;
        this.success = success;
    }

}
