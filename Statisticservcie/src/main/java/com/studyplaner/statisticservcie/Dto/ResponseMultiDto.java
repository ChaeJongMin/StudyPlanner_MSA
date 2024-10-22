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
}