package com.studyplanner.userservice.Feign;

import com.studyplanner.userservice.dto.request.RequsetStatisticDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "statistic-service")
public interface StatisticServiceClient {

    @GetMapping("/allStatistics/{userId}")
    RequsetStatisticDto requestGetMultipleStatisticInformation(@PathVariable Long userId);
}
