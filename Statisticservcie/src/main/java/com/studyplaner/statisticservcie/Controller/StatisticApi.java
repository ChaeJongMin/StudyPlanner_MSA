package com.studyplaner.statisticservcie.Controller;

import com.studyplaner.statisticservcie.Dto.ResponseMultiDto;
import com.studyplaner.statisticservcie.Dto.ResponseSingleDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatisticApi {

    //전체 정보 요청
    @GetMapping("/singleStatistics")
    public ResponseEntity<ResponseSingleDto> getSingleStatistics(@RequestParam long userId){

    }

    //모든 정보 요창
    @GetMapping("/allStatistics")
    public ResponseEntity<ResponseMultiDto> getAllStatistics(@RequestParam long userId){

    }
}
