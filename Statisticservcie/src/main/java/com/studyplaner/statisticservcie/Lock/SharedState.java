package com.studyplaner.statisticservcie.Lock;

import com.studyplaner.statisticservcie.Entity.StatisticTodoEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Getter
@Setter
@Component
public class SharedState {

    private boolean flag;

    // Optional을 담을 스레드 안전 큐
    private final Queue<Optional<StatisticTodoEntity>> entityQueue = new ConcurrentLinkedQueue<>();

    public SharedState(){
        this.flag = false;
    }



    public synchronized boolean processQueue() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String currentDate = LocalDate.now().format(dateFormatter);

        while (!entityQueue.isEmpty()) {
            Optional<StatisticTodoEntity> entityOpt = entityQueue.poll();
            entityOpt.ifPresent(entity -> entity.updateDateAndCnt(currentDate, 1));
        }
        return true;
    }

    public boolean emptyCheck(){
        return entityQueue.isEmpty();
    }
}
