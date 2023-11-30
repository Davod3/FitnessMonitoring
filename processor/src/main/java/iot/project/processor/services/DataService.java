package iot.project.processor.services;

import iot.project.processor.dtos.*;
import iot.project.processor.handlers.ActivityLevelHandler;
import iot.project.processor.handlers.CaloriesHandler;
import iot.project.processor.handlers.DataHandler;
import iot.project.processor.handlers.DistanceHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public class DataService {

    @Autowired private DataHandler dataHandler;
    @Autowired private CaloriesHandler caloriesHandler;
    @Autowired private DistanceHandler distanceHandler;
    @Autowired private ActivityLevelHandler activityHandler;

    public DataResponseDTO fetchData(DataRequestDTO request) {

        String dataType = request.getDataType();
        String dataPeriod = request.getDataPeriod();

        LocalDate parsedStartDate = parseDate(request.getStart_date());
        LocalDate parsedEndDate = parseDate(request.getEnd_date());

        if(dataType.equalsIgnoreCase(DataType.DURATION.toString())) {

                if(dataPeriod.equalsIgnoreCase(DataPeriod.DAY.toString())) {

                    DataResponse<String, Long> r = this.dataHandler.fetchDurationByDay(parsedStartDate,
                            parsedEndDate);
                    return dtofy(r.getDates(), r.getInformationRunning(), r.getInformationWalking());

                } else if (dataPeriod.equalsIgnoreCase(DataPeriod.WEEK.toString())) {

                    DataResponse<String, Long> r = this.dataHandler.fetchDurationByWeek(parsedStartDate,
                            parsedEndDate);
                    return dtofy(r.getDates(), r.getInformationRunning(), r.getInformationWalking());

                } else if (dataPeriod.equalsIgnoreCase(DataPeriod.MONTH.toString())) {

                   DataResponse<String, Long> r = this.dataHandler.fetchDurationByMonth(parsedStartDate,
                           parsedEndDate);
                   return dtofy(r.getDates(), r.getInformationRunning(), r.getInformationWalking());

                }

        } else if (dataType.equalsIgnoreCase(DataType.CALORIES.toString())) {

            if(dataPeriod.equalsIgnoreCase(DataPeriod.DAY.toString())) {

                DataResponse<String, Double> r = this.caloriesHandler.fetchCaloriesByDay(parsedStartDate, parsedEndDate, request.getAge(),
                        request.getHeight(), request.getWeight(), request.getGender());
                return dtofy(r.getDates(), r.getInformationRunning(), r.getInformationWalking());

            } else if (dataPeriod.equalsIgnoreCase(DataPeriod.WEEK.toString())) {

                DataResponse<String, Double> r = this.caloriesHandler.fetchCaloriesByWeek(parsedStartDate, parsedEndDate, request.getAge(),
                        request.getHeight(), request.getWeight(), request.getGender());
                return dtofy(r.getDates(), r.getInformationRunning(), r.getInformationWalking());

            } else if (dataPeriod.equalsIgnoreCase(DataPeriod.MONTH.toString())) {

                DataResponse<String, Double> r = this.caloriesHandler.fetchCaloriesByMonth(parsedStartDate, parsedEndDate, request.getAge(),
                        request.getHeight(), request.getWeight(), request.getGender());
                return dtofy(r.getDates(), r.getInformationRunning(), r.getInformationWalking());

            }

        } else if (dataType.equalsIgnoreCase(DataType.DISTANCE.toString())) {

            if(dataPeriod.equalsIgnoreCase(DataPeriod.DAY.toString())) {

                DataResponse<String, Double> r = this.distanceHandler.fetchDistanceByDay(parsedStartDate,
                        parsedEndDate, request.getAge(), request.getGender());

                return dtofy(r.getDates(), r.getInformationRunning(), r.getInformationWalking());

            } else if (dataPeriod.equalsIgnoreCase(DataPeriod.WEEK.toString())) {

                DataResponse<String, Double> r = this.distanceHandler.fetchDistanceByWeek(parsedStartDate,
                        parsedEndDate, request.getAge(), request.getGender());

                return dtofy(r.getDates(), r.getInformationRunning(), r.getInformationWalking());

            } else if (dataPeriod.equalsIgnoreCase(DataPeriod.MONTH.toString())) {

                DataResponse<String, Double> r = this.distanceHandler.fetchDistanceByMonth(parsedStartDate,
                        parsedEndDate, request.getAge(), request.getGender());

                return dtofy(r.getDates(), r.getInformationRunning(), r.getInformationWalking());

            }

        } else if (dataType.equalsIgnoreCase(DataType.ACTIVITY_LEVEL.toString())) {

            if(dataPeriod.equalsIgnoreCase(DataPeriod.DAY.toString())) {

                this.activityHandler.fetchActivityLevelByDay(parsedStartDate, parsedEndDate,
                        request.getWalking_threshold(), request.getRunning_threshold());

            } else if (dataPeriod.equalsIgnoreCase(DataPeriod.WEEK.toString())) {

                this.activityHandler.fetchActivityLevelByWeek(parsedStartDate, parsedEndDate,
                        request.getWalking_threshold(), request.getRunning_threshold());

            } else if (dataPeriod.equalsIgnoreCase(DataPeriod.MONTH.toString())) {

                this.activityHandler.fetchActivityLevelByMonth(parsedStartDate, parsedEndDate,
                        request.getWalking_threshold(), request.getRunning_threshold());

            }

        }

        return null;
    }

    private <T,O> DataResponseDTO dtofy(List<T> dates, List<O> runningInformation, List<O> walkingInformation) {

        List<String> series = new LinkedList<>();

        series.add("Running");
        series.add("Walking");

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

}
