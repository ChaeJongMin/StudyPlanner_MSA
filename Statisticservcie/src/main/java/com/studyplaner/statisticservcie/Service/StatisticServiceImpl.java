package com.studyplaner.statisticservcie.Service;

import com.studyplaner.statisticservcie.Dto.ResponseMultiDto;
import com.studyplaner.statisticservcie.Dto.ResponseSingleDto;
import com.studyplaner.statisticservcie.Entity.StatisticEntity;
import com.studyplaner.statisticservcie.Error.CustomException;
import com.studyplaner.statisticservcie.Lock.SharedState;
import com.studyplaner.statisticservcie.Repository.StatisticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements  StatisticService{

    private final StatisticRepository statisticRepository;
    private final SharedState sharedState;
    @Override
    public ResponseSingleDto getSingleStatisticInfo(long userId) {
        StatisticEntity statisticEntity = statisticRepository.findByUserId(userId).orElseThrow(
                ()-> new CustomException("NOT_EXIST_USER","해당 유저는 존재하지 않습니다.")
        );

        return statisticEntity.toResponseSingleDto();
    }

    @Override
    public ResponseMultiDto getMultipleStatisticInfo(long userId) {
        if(!sharedState.isFlag()){
            StatisticEntity statisticEntity = statisticRepository.findByUserId(userId).orElseThrow(
                    ()-> new CustomException("NOT_EXIST_USER","해당 유저는 존재하지 않습니다.")
            );
            int updateCount = statisticEntity.getStatisticTodo().getTodayCount();
            if(updateCount > 0){
                statisticEntity.updateSuccessTodoCnt(updateCount);
            }
            statisticEntity.getStatisticTodo().init("");
            return statisticEntity.toResponseMultiDto();

        }

        //예외 발생 (정각 이후로 접속)
        throw new CustomException("WAIT_MYPAGE_CONNECT", "통계 수정 중입니다. 00:01 이후에 다시 접속해주세요");
    }
}
