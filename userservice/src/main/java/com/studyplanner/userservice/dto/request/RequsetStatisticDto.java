package com.studyplanner.userservice.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Normalized;

@Getter
@Builder
public class RequsetStatisticDto {

    private double totalSuccessRate;
    private double dailySuccessRate;
    private double weeklySuccessRate;
    private double monthlySuccessRate;
    private int totalCount = 0;
    private int successCount = 0;
    private int failCount = 0;
}
