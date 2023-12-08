package iot.project.processor.handlers;

import iot.project.processor.dtos.DataResponse;
import iot.project.processor.entities.ProcessedUserData;
import iot.project.processor.entities.Week;
import iot.project.processor.repositories.SavedUserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DurationHandler {

    @Autowired private SavedUserDataRepository userDataRepo;

    public DataResponse<String, Long> fetchDurationByDay(LocalDate startDate, LocalDate endDate) {

        List<String> series = new LinkedList<>();
        series.add("Duration Running (Minutes)");
        series.add("Duration Walking (Minutes)");

        List<LocalDate> days = getDaysBetween(startDate, endDate);
        List<String> dates = new LinkedList<>();

        List<Long> durationsRunning = new LinkedList<>();
        List<Long> durationsWalking = new LinkedList<>();

        for(LocalDate d : days) {

            dates.add(d.toString());

            ProcessedUserData data = this.userDataRepo.findByDate(d);

            if(data == null) {
                durationsRunning.add(0L);
                durationsWalking.add(0L);
            } else {
                durationsRunning.add(data.getRunningDuration());
                durationsWalking.add(data.getWalkingDuration());
            }

        }

        return new DataResponse<String, Long>(dates, durationsRunning, durationsWalking, series);

    }

    public DataResponse<String, Long> fetchDurationByWeek(LocalDate startDate, LocalDate endDate) {

        List<String> series = new LinkedList<>();
        series.add("Duration Running (Minutes)");
        series.add("Duration Walking (Minutes)");

        List<String> dates = new LinkedList<>();

        List<Long> durationWalking = new LinkedList<>();
        List<Long> durationRunning = new LinkedList<>();

        List<Week> weeks = getWeeksBetween(startDate, endDate);

        for (Week w : weeks) {

            DataResponse<String, Long> response = fetchDurationByDay(w.getStart(), w.getEnd());

            dates.add(w.getStart().toString());

            long totalWalking = 0;

            for(long l : response.getInformationWalking()) {
                totalWalking+=l;
            }

            durationWalking.add(totalWalking);

            long totalRunning = 0;

            for(long l : response.getInformationRunning()) {
                totalRunning+=l;
            }

            durationRunning.add(totalRunning);

        }

        return new DataResponse<String, Long>(dates, durationRunning, durationWalking, series);

    }

    public DataResponse<String, Long> fetchDurationByMonth(LocalDate startDate, LocalDate endDate) {

        List<String> series = new LinkedList<>();
        series.add("Duration Running (Minutes)");
        series.add("Duration Walking (Minutes)");

        List<String> dates = new LinkedList<>();

        List<Long> durationWalking = new LinkedList<>();
        List<Long> durationRunning = new LinkedList<>();

        List<Month> monthsBetween = getMonthsBetween(startDate, endDate);
        LocalDate start = startDate;
        LocalDate end;

        for(Month m : monthsBetween) {

            YearMonth yearMonth = YearMonth.of(start.getYear(), m.getValue());
            LocalDate endOfMonth = yearMonth.atEndOfMonth();

            if(endOfMonth.isAfter(endDate)) {
                end = endDate;
            } else {
                end = endOfMonth;
            }

            DataResponse<String, Long> durationPerDay = fetchDurationByDay(start, end);

            long totalRunning = 0;

            for(long l : durationPerDay.getInformationRunning()) {
                totalRunning+=l;
            }

            long totalWalking = 0;

            for(long l : durationPerDay.getInformationWalking()) {
                totalWalking+=l;
            }

            dates.add(m.toString() + " " + yearMonth.getYear());

            durationRunning.add(totalRunning);

            durationWalking.add(totalWalking);

            start = endOfMonth.plusDays(1);

        }

        return new DataResponse<String, Long>(dates, durationRunning, durationWalking, series);

    }

    private List<LocalDate> getDaysBetween(LocalDate startDate, LocalDate endDate) {

        if(startDate.isEqual(endDate)) {

            List<LocalDate> result = new LinkedList<>();
            result.add(startDate);

            return result;
        }

        return startDate.datesUntil(endDate).collect(Collectors.toList());
    }

    private List<Month> getMonthsBetween(LocalDate startDate, LocalDate endDate) {
        List<Month> monthsBetween = new ArrayList<>();
        LocalDate currentMonth = startDate;

        while (!currentMonth.isAfter(endDate)) {
            monthsBetween.add(currentMonth.getMonth());
            currentMonth = currentMonth.plusMonths(1);
        }

        return monthsBetween;
    }

    private List<Week> getWeeksBetween(LocalDate startDate, LocalDate endDate) {

        List<Week> result = new LinkedList<>();

        LocalDate start = startDate;
        LocalDate currentDate = start;

        while (currentDate.isBefore(endDate)) {

            if(currentDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                Week w = new Week(start, currentDate);
                result.add(w);
                LocalDate nextDate = currentDate.plusDays(1);
                start = nextDate;
                currentDate = nextDate;

            } else {

                LocalDate nextDate = currentDate.plusDays(1);
                currentDate = nextDate;

            }

        }

        if(currentDate.isEqual(endDate)) {
            Week w = new Week(start, currentDate);
            result.add(w);
        }

        return result;

    }

}
