package com.studyplaner.statisticservcie.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@Component
@RequiredArgsConstructor
@Slf4j
public class StatisticUtil {


    public double calculateTotalSuccessRate(int totalCnt, int successCnt) {
        if (totalCnt == 0) {
            return 0;
        }

        double successRate = ((double) successCnt / totalCnt) * 100;

        // 소수 첫째자리까지 버림
        return Math.floor(successRate * 10) / 10.0;
    }

    public boolean isNewDay(String targetDate,String compareDate) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd:HH:mm:ss");

        LocalDate targetLocalDate = LocalDate.parse(targetDate, formatter);
        LocalDate compareLocalDate = LocalDate.parse(compareDate, formatter);

        return targetLocalDate.isEqual(compareLocalDate);
    }


    // 월 변경 여부 확인
    public boolean isNewMonth(String targetDate,String compareDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd:HH:mm:ss");

        LocalDate targetLocalDate = LocalDate.parse(targetDate, formatter);
        LocalDate compareLocalDate = LocalDate.parse(compareDate, formatter);

        return targetLocalDate.getMonth() == compareLocalDate.getMonth();
    }

    public String getCurrentWeekOfMonth(String dateStr) {

        Date date = new Date();
        try{
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = format.parse(dateStr);

        } catch (ParseException e){
            e.fillInStackTrace();
        }
        Calendar calendar = Calendar.getInstance(Locale.KOREA);
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1; // calendar에서의 월은 0부터 시작
        int day = calendar.get(Calendar.DATE);
        int year = calendar.get(Calendar.YEAR);
        // 한 주의 시작은 월요일이고, 첫 주에 4일이 포함되어있어야 첫 주 취급 (목/금/토/일)
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setMinimalDaysInFirstWeek(4);

        int weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH);

        // 첫 주에 해당하지 않는 주의 경우 전 달 마지막 주차로 계산
        if (weekOfMonth == 0) {
            calendar.add(Calendar.DATE, -day); // 전 달의 마지막 날 기준
            return getCurrentWeekOfMonth(calendar.getTime().toString());
        }

        // 마지막 주차의 경우
        if (weekOfMonth == calendar.getActualMaximum(Calendar.WEEK_OF_MONTH)) {
            calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE)); // 이번 달의 마지막 날
            int lastDaysDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK); // 이번 달 마지막 날의 요일

            // 마지막 날이 월~수 사이이면 다음달 1주차로 계산
            if (lastDaysDayOfWeek >= Calendar.MONDAY && lastDaysDayOfWeek <= Calendar.WEDNESDAY) {
                calendar.add(Calendar.DATE, 1); // 마지막 날 + 1일 => 다음달 1일
                return getCurrentWeekOfMonth(calendar.getTime().toString());
            }
        }

        return (year+"-"+month+"-"+day+"-"+weekOfMonth);
    }



}
