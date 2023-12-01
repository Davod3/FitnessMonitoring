package iot.project.processor.services;

import iot.project.processor.dtos.*;
import iot.project.processor.handlers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

@Component
public class DataService {

    @Autowired private DurationHandler durationHandler;
    @Autowired private CaloriesHandler caloriesHandler;
    @Autowired private DistanceHandler distanceHandler;
    @Autowired private ActivityLevelHandler activityHandler;

    public DataResponseDTO fetchDuration(DataRequestDTO request) {

        String dataPeriod = request.getDataPeriod();

        LocalDate parsedStartDate = parseDate(request.getStart_date());
        LocalDate parsedEndDate = parseDate(request.getEnd_date());

        if(dataPeriod.equalsIgnoreCase(DataPeriod.DAY.toString())) {

            DataResponse<String, Long> r = this.durationHandler.fetchDurationByDay(parsedStartDate,
                    parsedEndDate);

            return dtofy(r.getDates(), convertDurations(r.getInformationRunning()),
                    convertDurations(r.getInformationWalking()), r.getSeries());

        } else if (dataPeriod.equalsIgnoreCase(DataPeriod.WEEK.toString())) {

            DataResponse<String, Long> r = this.durationHandler.fetchDurationByWeek(parsedStartDate,
                    parsedEndDate);

            return dtofy(r.getDates(),convertDurations(r.getInformationRunning()),
                    convertDurations(r.getInformationWalking()), r.getSeries());

        } else if (dataPeriod.equalsIgnoreCase(DataPeriod.MONTH.toString())) {

            DataResponse<String, Long> r = this.durationHandler.fetchDurationByMonth(parsedStartDate,
                    parsedEndDate);

            return dtofy(r.getDates(), convertDurations(r.getInformationRunning()),
                    convertDurations(r.getInformationWalking()), r.getSeries());

        }

        return null;

    }

    public DataResponseDTO fetchCalories(DataRequestDTO request) {

        String dataPeriod = request.getDataPeriod();

        LocalDate parsedStartDate = parseDate(request.getStart_date());
        LocalDate parsedEndDate = parseDate(request.getEnd_date());

        if(dataPeriod.equalsIgnoreCase(DataPeriod.DAY.toString())) {

            DataResponse<String, Double> r = this.caloriesHandler.fetchCaloriesByDay(parsedStartDate, parsedEndDate, request.getAge(),
                    request.getHeight(), request.getWeight(), request.getGender());
            return dtofy(r.getDates(), r.getInformationRunning(), r.getInformationWalking(), r.getSeries());

        } else if (dataPeriod.equalsIgnoreCase(DataPeriod.WEEK.toString())) {

            DataResponse<String, Double> r = this.caloriesHandler.fetchCaloriesByWeek(parsedStartDate, parsedEndDate, request.getAge(),
                    request.getHeight(), request.getWeight(), request.getGender());
            return dtofy(r.getDates(), r.getInformationRunning(), r.getInformationWalking(), r.getSeries());

        } else if (dataPeriod.equalsIgnoreCase(DataPeriod.MONTH.toString())) {

            DataResponse<String, Double> r = this.caloriesHandler.fetchCaloriesByMonth(parsedStartDate, parsedEndDate, request.getAge(),
                    request.getHeight(), request.getWeight(), request.getGender());
            return dtofy(r.getDates(), r.getInformationRunning(), r.getInformationWalking(), r.getSeries());

        }

        return null;

    }

    public DataResponseDTO fetchDistance(DataRequestDTO request) {

        String dataPeriod = request.getDataPeriod();

        LocalDate parsedStartDate = parseDate(request.getStart_date());
        LocalDate parsedEndDate = parseDate(request.getEnd_date());

        if(dataPeriod.equalsIgnoreCase(DataPeriod.DAY.toString())) {

            DataResponse<String, Double> r = this.distanceHandler.fetchDistanceByDay(parsedStartDate,
                    parsedEndDate, request.getAge(), request.getGender());

            return dtofy(r.getDates(), r.getInformationRunning(), r.getInformationWalking(), r.getSeries());

        } else if (dataPeriod.equalsIgnoreCase(DataPeriod.WEEK.toString())) {

            DataResponse<String, Double> r = this.distanceHandler.fetchDistanceByWeek(parsedStartDate,
                    parsedEndDate, request.getAge(), request.getGender());

            return dtofy(r.getDates(), r.getInformationRunning(), r.getInformationWalking(), r.getSeries());

        } else if (dataPeriod.equalsIgnoreCase(DataPeriod.MONTH.toString())) {

            DataResponse<String, Double> r = this.distanceHandler.fetchDistanceByMonth(parsedStartDate,
                    parsedEndDate, request.getAge(), request.getGender());

            return dtofy(r.getDates(), r.getInformationRunning(), r.getInformationWalking(), r.getSeries());

        }

        return null;
    }

    public DataResponseDTO fetchActivityLevel(DataRequestDTO request) {

        String dataPeriod = request.getDataPeriod();

        LocalDate parsedStartDate = parseDate(request.getStart_date());
        LocalDate parsedEndDate = parseDate(request.getEnd_date());

        if(dataPeriod.equalsIgnoreCase(DataPeriod.DAY.toString())) {

            DataResponse<String, Integer> r = this.activityHandler.fetchActivityLevelByDay(parsedStartDate, parsedEndDate,
                    request.getWalking_threshold(), request.getRunning_threshold());

            return dtofy(r.getDates(), r.getInformationRunning(), r.getInformationWalking(), r.getSeries());

        } else if (dataPeriod.equalsIgnoreCase(DataPeriod.WEEK.toString())) {

            DataResponse<String, Integer> r = this.activityHandler.fetchActivityLevelByWeek(parsedStartDate, parsedEndDate,
                    request.getWalking_threshold(), request.getRunning_threshold());

            return dtofy(r.getDates(), r.getInformationRunning(), r.getInformationWalking(), r.getSeries());

        } else if (dataPeriod.equalsIgnoreCase(DataPeriod.MONTH.toString())) {

            DataResponse<String, Integer> r = this.activityHandler.fetchActivityLevelByMonth(parsedStartDate, parsedEndDate,
                    request.getWalking_threshold(), request.getRunning_threshold());

            return dtofy(r.getDates(), r.getInformationRunning(), r.getInformationWalking(), r.getSeries());

        }

        return null;

    }

    private <T,O> DataResponseDTO dtofy(List<T> dates, List<O> runningInformation, List<O> walkingInformation, List<String> series) {

        List<String> runningDurations = new LinkedList<>();

        for(O o : runningInformation) {
            runningDurations.add(o.toString());
        }

        List<String> walkingDurations = new LinkedList<>();

        for(O o : walkingInformation) {
            walkingDurations.add(o.toString());
        }

        List<String[]> data = new LinkedList<>();

        data.add(runningDurations.toArray(new String[0]));
        data.add(walkingDurations.toArray(new String[0]));

        List<String> labels = new LinkedList<>();

        for(T t : dates){

            labels.add(t.toString());
        }

        return new DataResponseDTO(series, data, labels);

    }

    private LocalDate parseDate(String dateString) {
        return LocalDateTime.parse(dateString, DateTimeFormatter.ISO_DATE_TIME).toLocalDate();
    }

    private List<Double> convertDurations(List<Long> durations) {

        List<Double> convertedDurations = new LinkedList<>();

        for(long l : durations) {

            Double d = BigDecimal.valueOf(l / 60.0).setScale(2, RoundingMode.HALF_UP).doubleValue();
            convertedDurations.add(d);
        }

        return convertedDurations;

    }

}
