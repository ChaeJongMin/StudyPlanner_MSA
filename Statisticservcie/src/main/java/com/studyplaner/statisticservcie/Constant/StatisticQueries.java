package com.studyplaner.statisticservcie.Constant;

public class StatisticQueries {
    public static final String UPDATE_STATISTICS =
            "UPDATE StatisticEntity se " +
                    "SET se.successCnt = se.successCnt + st.count, " +
                    "    se.dailySuccessCnt = CASE " +
                    "        WHEN CURRENT_DATE <> DATE_ADD(CURDATE(), INTERVAL -1 DAY) THEN 0 " +
                    "        ELSE se.dailySuccessCnt + st.count " +
                    "    END, " +
                    "    se.weeklySuccessCnt = CASE " +
                    "        WHEN WEEK(CURRENT_DATE) <> WEEK(CURDATE() - INTERVAL 1 WEEK) THEN 0 " +
                    "        ELSE se.weeklySuccessCnt + st.count " +
                    "    END, " +
                    "    se.monthlySuccessCnt = CASE " +
                    "        WHEN MONTH(CURRENT_DATE) <> MONTH(CURDATE() - INTERVAL 1 MONTH) THEN 0 " +
                    "        ELSE se.monthlySuccessCnt + st.count " +
                    "    END, " +
                    "    se.dailyTotalTodoCount = CASE " +
                    "        WHEN CURRENT_DATE <> DATE_ADD(CURDATE(), INTERVAL -1 DAY) THEN 0 " +
                    "        ELSE se.dailyTotalTodoCount " +
                    "    END, " +
                    "    se.weeklyTotalTodoCount = CASE " +
                    "        WHEN WEEK(CURRENT_DATE) <> WEEK(CURDATE() - INTERVAL 1 WEEK) THEN 0 " +
                    "        ELSE se.weeklyTotalTodoCount " +
                    "    END, " +
                    "    se.monthlyTotalTodoCount = CASE " +
                    "        WHEN MONTH(CURRENT_DATE) <> MONTH(CURDATE() - INTERVAL 1 MONTH) THEN 0 " +
                    "        ELSE se.monthlyTotalTodoCount " +
                    "    END " +
                    "WHERE se.id IN (SELECT st.statisticEntity.id FROM StatisticTodoEntity st WHERE st.count > 0)";
}