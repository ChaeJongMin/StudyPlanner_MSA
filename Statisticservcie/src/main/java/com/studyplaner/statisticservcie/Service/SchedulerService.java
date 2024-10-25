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

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchedulerService {

    private final StatisticTodoRepository statisticTodoRepository;
    private final StatisticRepository statisticRepository;

    @Transactional
    @Scheduled(cron = "0 0 */2 * * ?")
    public void runForCompleteTodo() {
        LocalTime now = LocalTime.now();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String todayDateString = today.format(formatter);

        // 오늘 날짜 기준 주와 월이 변경되었는지 확인
        boolean isNewWeek = isNewWeek(today);
        boolean isNewMonth = isNewMonth(today);

        List<StatisticEntity> statisticEntities = statisticRepository.findAll();

        for (StatisticEntity entity : statisticEntities) {
            StatisticTodoEntity todoEntity = entity.getStatisticTodo();
            int totalCount = 0;

            if (now.equals(LocalTime.MIDNIGHT)) {
                // 현재 시간이 00:00일 때
                String todoDate = todoEntity.getDate();
                int yesterdayCount = !todoDate.equals(todayDateString) ? todoEntity.getTodayCount() : todoEntity.getYesterdayCount();
                totalCount = !todoDate.equals(todayDateString) ? yesterdayCount : yesterdayCount + todoEntity.getTodayCount();

                String change = "";
                if(isNewMonth){
                    change = "month";
                } else if(isNewWeek){
                    change = "week";
                }
                entity.updateSuccessTodoCntToChangeCondition(totalCount,change);
                todoEntity.init(todayDateString);
            } else {
                // 00:00이 아닐 때
                totalCount = todoEntity.getTodayCount();
                entity.updateSuccessTodoCnt(totalCount);
                todoEntity.init("");
            }
        }
    }

    // 주 변경 여부 확인
    private boolean isNewWeek(LocalDate date) {
        LocalDate previousDay = date.minusDays(1);
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int currentWeek = date.get(weekFields.weekOfWeekBasedYear());
        int previousWeek = previousDay.get(weekFields.weekOfWeekBasedYear());
        return currentWeek != previousWeek;
    }

    // 월 변경 여부 확인
    private boolean isNewMonth(LocalDate date) {
        LocalDate previousDay = date.minusDays(1);
        return date.getMonth() != previousDay.getMonth();
    }
}
