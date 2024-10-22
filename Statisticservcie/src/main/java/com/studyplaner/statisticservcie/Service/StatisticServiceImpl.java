package com.studyplaner.statisticservcie.Service;

import com.studyplaner.statisticservcie.Dto.ResponseSingleDto;
import com.studyplaner.statisticservcie.Entity.StatisticEntity;
import com.studyplaner.statisticservcie.Repository.StatisticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements  StatisticService{

    private final StatisticRepository statisticRepository;
    @Override
    public ResponseSingleDto getSingleStatisticInfo(long userId) {
        StatisticEntity statisticEntity = statisticRepository.findByUserId(userId).orElseThrow(
                ()-> new RuntimeException("해당 유저에 대한 정보가 없습니다.")
        );

        return statisticEntity.toResponseSingleDto();
    }
}
