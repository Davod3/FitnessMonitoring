package iot.project.processor.handlers;

import iot.project.processor.dtos.DataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Component
public class ActivityLevelHandler {

    @Autowired
    private DataHandler dataHandler;

    private final int WEEK_MULTIPLIER = 7;
    private final int MONTH_MULTIPLIER = 30;
    private final int DAY_MULTIPLIER = 1;

    public DataResponse<String, Integer> fetchActivityLevelByDay(LocalDate startDate, LocalDate endDate,
                                                                 double thresholdWalking, double thresholdRunning) {

        DataResponse<String, Long> durations = this.dataHandler.fetchDurationByDay(startDate, endDate);

        return fetchActivityLevel(durations, thresholdWalking*60, thresholdRunning*60, DAY_MULTIPLIER);

    }

    public DataResponse<String, Integer> fetchActivityLevelByWeek(LocalDate startDate, LocalDate endDate,
                                                                  double thresholdWalking, double thresholdRunning) {

        DataResponse<String, Long> durations = this.dataHandler.fetchDurationByWeek(startDate, endDate);

        return fetchActivityLevel(durations, thresholdWalking*60, thresholdRunning*60, WEEK_MULTIPLIER);

    }

    public DataResponse<String, Integer> fetchActivityLevelByMonth(LocalDate startDate, LocalDate endDate,
                                                                   double thresholdWalking, double thresholdRunning) {

        DataResponse<String, Long> durations = this.dataHandler.fetchDurationByMonth(startDate, endDate);

        return fetchActivityLevel(durations, thresholdWalking*60, thresholdRunning*60, MONTH_MULTIPLIER);

    }

    private DataResponse<String, Integer> fetchActivityLevel(DataResponse<String, Long> durations, double thresholdWalking,
                                                             double thresholdRunning, int multiplier) {

        List<Integer> activeDates = new LinkedList<>();
        List<Integer> sedentaryDates = new LinkedList<>();

        for(int i = 0; i < durations.getDates().size(); i++) {

            long runningDuration = durations.getInformationRunning().get(i);
            long walkingDuration = durations.getInformationWalking().get(i);

            if(isActive(runningDuration, walkingDuration,
                    multiplier * thresholdRunning, multiplier * thresholdWalking)) {

                activeDates.add(1);
                sedentaryDates.add(0);

            } else {
                activeDates.add(0);
                sedentaryDates.add(1);
            }
        }


        return new DataResponse<String, Integer>(durations.getDates(), activeDates, sedentaryDates);

    }

    private boolean isActive(long runningDuration, long walkingDuration, double thresholdRunning, double thresholdWalking) {

        return runningDuration >= thresholdRunning || walkingDuration >= thresholdWalking;
    }


}
