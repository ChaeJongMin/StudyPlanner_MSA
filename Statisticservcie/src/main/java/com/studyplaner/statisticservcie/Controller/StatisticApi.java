package com.studyplaner.statisticservcie.Controller;

import com.studyplaner.statisticservcie.Dto.ResponseMultiDto;
import com.studyplaner.statisticservcie.Dto.ResponseSingleDto;
import com.studyplaner.statisticservcie.Service.StatisticServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatisticApi {

    private final StatisticServiceImpl statisticService;
    //전체 정보 요청
    @GetMapping("/singleStatistics/{userId}")
    public ResponseEntity<ResponseSingleDto> getSingleStatistics(@PathVariable long userId){

        return ResponseEntity.status(200).body(statisticService.getSingleStatisticInfo(userId));
    }

    //모든 정보 요창
    @GetMapping("/allStatistics/{userId}")
    public ResponseEntity<ResponseMultiDto> getAllStatistics(@PathVariable Long userId){

        return ResponseEntity.status(200).body(statisticService.getMultipleStatisticInfo(userId));
    }
}
