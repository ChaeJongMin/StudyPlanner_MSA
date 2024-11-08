package com.studyplaner.statisticservcie.Service;

import com.studyplaner.statisticservcie.Dto.ResponseMultiDto;
import com.studyplaner.statisticservcie.Dto.ResponseSingleDto;
import com.studyplaner.statisticservcie.Dto.Sql.ThisTotalDto;
import com.studyplaner.statisticservcie.Entity.StatisticDetailEntity;
import com.studyplaner.statisticservcie.Entity.StatisticTotalEntity;
import com.studyplaner.statisticservcie.Error.CustomException;
import com.studyplaner.statisticservcie.Repository.StatisticDetailRepository;
import com.studyplaner.statisticservcie.Repository.StatisticTotalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements  StatisticService{

    private final StatisticTotalRepository statisticTotalRepository;
    private final StatisticDetailRepository statisticDetailRepository;
    private final StatisticUtil statisticUtil;
    @Override
    public ResponseSingleDto getSingleStatisticInfo(long userId) {
        StatisticTotalEntity statisticTotalEntity = statisticTotalRepository.findByUserId(userId).orElseThrow(
                ()-> new CustomException("NOT_EXIST_USER","해당 유저는 존재하지 않습니다.")
        );

        int totalCnt = statisticTotalEntity.getCreateCount();
        int successCnt = statisticTotalEntity.getSuccessCnt();
        return ResponseSingleDto.builder()
                .totalCnt(totalCnt)
                .successCnt(successCnt)
                .failCnt(totalCnt-successCnt)
                .totalSuccessRate(statisticUtil.calculateTotalSuccessRate(totalCnt,successCnt))
                .build();
    }

    @Override
    public ResponseMultiDto getMultipleStatisticInfo(long userId) {
        StatisticTotalEntity statisticTotalEntity = statisticTotalRepository.findByUserId(userId).orElseThrow(
                ()-> new CustomException("NOT_EXIST_USER","해당 유저는 존재하지 않습니다.")
        );

        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = localDateTime.format(formatter);

        String convertDate = statisticUtil.getCurrentWeekOfMonth(formattedDate);

        StatisticDetailEntity statisticDetailEntity = statisticDetailRepository.findByUserIdAndDate(userId,convertDate).orElseThrow(
                ()-> new CustomException("NOT_EXIST_USER","해당 유저는 존재하지 않습니다.")
        );

        ThisTotalDto thisMonthTotalDto = statisticDetailRepository.findMonthlySummary(userId,convertDate);

        String weekOfMonth = convertDate.split("-")[3];
        ThisTotalDto thisWeeklyTotalDto = statisticDetailRepository.findWeeklySummary(userId,convertDate,weekOfMonth);


        return ResponseMultiDto.builder()
                .totalCount(statisticTotalEntity.getCreateCount())
                .successCount(statisticTotalEntity.getSuccessCnt())
                .totalSuccessRate(statisticUtil.calculateTotalSuccessRate(
                        statisticTotalEntity.getCreateCount()
                        ,statisticTotalEntity.getSuccessCnt()))
                .dailySuccessRate(statisticUtil.calculateTotalSuccessRate(
                        statisticDetailEntity.getCreateCnt(),
                        statisticDetailEntity.getSuccessCnt()
                        ))
                .weeklySuccessRate(statisticUtil.calculateTotalSuccessRate(
                        thisWeeklyTotalDto.getTotal().intValue(),
                        thisWeeklyTotalDto.getSuccess().intValue()
                ))
                .monthlySuccessRate(statisticUtil.calculateTotalSuccessRate(
                        thisMonthTotalDto.getTotal().intValue(),
                        thisMonthTotalDto.getSuccess().intValue()
                ))
                .build();

    }


}
