package com.studyplaner.statisticservcie.Service;

import com.studyplaner.statisticservcie.Dto.ResponseMultiDto;
import com.studyplaner.statisticservcie.Dto.ResponseSingleDto;

public interface StatisticService {

    ResponseSingleDto getSingleStatisticInfo(long userId);

    ResponseMultiDto getMultipleStatisticInfo(long userId);
}
