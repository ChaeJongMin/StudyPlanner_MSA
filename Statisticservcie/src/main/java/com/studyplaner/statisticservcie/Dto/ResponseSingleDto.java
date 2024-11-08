package com.studyplaner.statisticservcie.Dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseSingleDto {

    private int successCnt;
    private int failCnt;
    private int totalCnt;
    private double totalSuccessRate;


}
