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

    public void fetchActivityLevelByDay(LocalDate startDate, LocalDate endDate,
                                        double thresholdWalking, double thresholdRunning) {

        DataResponse<String, Long> durations = this.dataHandler.fetchDurationByDay(startDate, endDate);

        List<Integer> isActive = new LinkedList<>();

        for(int i = 0; i < durations.getDates().size(); i++) {

            long runningDuration = durations.getInformationRunning().get(i);
            long walkingDuration = durations.getInformationWalking().get(i);

            isActive.add(isActive(runningDuration, walkingDuration, thresholdRunning, thresholdWalking));
        }


        //Return activity level list

    }

    public void fetchActivityLevelByWeek(LocalDate startDate, LocalDate endDate,
                                         double thresholdWalking, double thresholdRunning) {



    }

    public void fetchActivityLevelByMonth(LocalDate startDate, LocalDate endDate,
                                          double thresholdWalking, double thresholdRunning) {



    }

    private Integer isActive(long runningDuration, long walkingDuration, double thresholdRunning, double thresholdWalking) {

        return runningDuration >= thresholdRunning || walkingDuration >= thresholdWalking?1:0;
    }

}
