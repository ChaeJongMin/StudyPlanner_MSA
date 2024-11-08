package com.studyplaner.statisticservcie.Service;

//import com.studyplaner.statisticservcie.Entity.StatisticTodoEntity;
//mport com.studyplaner.statisticservcie.Repository.StatisticRepository;
//import com.studyplaner.statisticservcie.Repository.StatisticTodoRepository;
//impimport com.studyplaner.statisticservcie.Lock.SharedState;
//iort jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.time.format.DateTimeFormatter;
//import java.time.temporal.WeekFields;
//import java.util.List;
//import java.util.Locale;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class SchedulerService {
//
//    private final StatisticTodoRepository statisticTodoRepository;
//    private final StatisticRepository statisticRepository;
//    private final SharedState sharedState;
//    @Transactional
//    @Scheduled(cron = "5 0 */2 * * ?")
//    public void runForCompleteTodo() {
//
//        LocalDate today = LocalDate.now();
//        LocalTime now = LocalTime.now();
//        String todayDateString = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//
//        List<StatisticEntity> statisticEntities = statisticRepository.findAll();
//
//        for (StatisticEntity entity : statisticEntities) {
//            StatisticTodoEntity todoEntity = entity.getStatisticTodo();
//            int totalCount = todoEntity.getTodayCount();
//            if(todoEntity.getTodayCount() ==0 && todoEntity.getYesterdayCount() == 0){
//                continue;
//            }
//            if (now.equals(LocalTime.MIDNIGHT)) {
//
//                boolean isNewWeek = isNewWeek(today);
//                boolean isNewMonth = isNewMonth(today);
//                String changeCondition = (isNewMonth ? "month" : "") + (isNewWeek ? "week" : "");
//
//                entity.updateSuccessTodoCntToChangeCondition(totalCount, changeCondition);
//                todoEntity.init(todayDateString);
//
//                sharedState.setFlag(false);
//            } else {
//                entity.updateSuccessTodoCnt(totalCount);
//                todoEntity.init("");
//            }
//        }
//    }
//
//    // 주 변경 여부 확인
//    private boolean isNewWeek(LocalDate date) {
//        LocalDate previousDay = date.minusDays(1);
//        WeekFields weekFields = WeekFields.of(Locale.getDefault());
//        int currentWeek = date.get(weekFields.weekOfWeekBasedYear());
//        int previousWeek = previousDay.get(weekFields.weekOfWeekBasedYear());
//        return currentWeek != previousWeek;
//    }
//
//    // 월 변경 여부 확인
//    private boolean isNewMonth(LocalDate date) {
//        LocalDate previousDay = date.minusDays(1);
//        return date.getMonth() != previousDay.getMonth();
//    }
//
//    @Async
//    @Scheduled(cron = "30 59 23 * * *")
//    public void toggleVariable() {
//        sharedState.setFlag(true);
//    }
//
//}
