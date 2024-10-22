package com.studyplaner.statisticservcie.Service;

import com.studyplaner.statisticservcie.Entity.StatisticEntity;
import com.studyplaner.statisticservcie.Entity.StatisticTodoEntity;
import com.studyplaner.statisticservcie.Repository.StatisticRepository;
import com.studyplaner.statisticservcie.Repository.StatisticTodoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchedulerService {

    private final StatisticTodoRepository statisticTodoRepository;
    private final StatisticRepository statisticRepository;

    @Scheduled(cron = "0 */3 * * * ?")
    public void runForCompleteTodo(){
        //데이터를 가져와서 업데이트
        //1. 리스트 가져오기
        List<StatisticTodoEntity> list = statisticTodoRepository.findAll();

        if(list.isEmpty()) //비어있으면 그냥 넘어간다.
            return;

        Set<Long> userIds = list.stream()
                .map(StatisticTodoEntity::getUserId)
                .collect(Collectors.toSet());

        // userId 리스트를 기반으로 StatisticEntity 가져오기
        List<StatisticEntity> statisticEntities = statisticRepository.findByUserIdIn(userIds);

        for (StatisticTodoEntity statisticTodo : list) {
            long userId = statisticTodo.getUserId();

            // 해당 userId에 대한 StatisticEntity 찾기
            statisticEntities.stream()
                    .filter(stat -> stat.getUserId() == userId)
                    .findFirst().ifPresent(statisticEntity -> statisticEntity.updateSuccessTodoCnt(statisticTodo.getCount()));

        }
        statisticRepository.saveAll(statisticEntities);
    }
}
