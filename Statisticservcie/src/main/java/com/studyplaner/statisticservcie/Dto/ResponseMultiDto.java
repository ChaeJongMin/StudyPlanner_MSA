package com.studyplaner.statisticservcie.Dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseMultiDto {

    private double totalSuccessRate;
    private double dailySuccessRate;
    private double weeklySuccessRate;
    private double monthlySuccessRate;
    private int totalCount = 0;
    private int successCount = 0;

}