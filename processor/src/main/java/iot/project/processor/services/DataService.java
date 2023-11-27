package iot.project.processor.services;

import iot.project.processor.dtos.*;
import iot.project.processor.handlers.DataHandler;
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

    public DataResponseDTO fetchData(DataRequestDTO request) {

        String dataType = request.getDataType();
        String dataPeriod = request.getDataPeriod();

        LocalDateTime parsedStartDate = parseDate(request.getStart_date());
        LocalDateTime parsedEndDate = parseDate(request.getEnd_date());

        if(dataType.equalsIgnoreCase(DataType.DURATION.toString())) {

                if(dataPeriod.equalsIgnoreCase(DataPeriod.DAY.toString())) {

                    DataResponse<LocalDate, Long> r = this.dataHandler.fetchDurationByDay(parsedStartDate.toLocalDate(),
                            parsedEndDate.toLocalDate());
                    return dtofy(r.getRunningData(), r.getWalkingData());

                } else if (dataPeriod.equalsIgnoreCase(DataPeriod.WEEK.toString())) {

                    this.dataHandler.fetchDurationByWeek();

                } else if (dataPeriod.equalsIgnoreCase(DataPeriod.MONTH.toString())) {

                   DataResponse<String, Long> r = this.dataHandler.fetchDurationByMonth(parsedStartDate,
                           parsedEndDate);
                   return dtofy(r.getRunningData(), r.getWalkingData());

                }

        } else if (dataType.equalsIgnoreCase(DataType.CALORIES.toString())) {

            if(dataPeriod.equalsIgnoreCase(DataPeriod.DAY.toString())) {

                this.dataHandler.fetchCaloriesByDay();

            } else if (dataPeriod.equalsIgnoreCase(DataPeriod.WEEK.toString())) {

                this.dataHandler.fetchCaloriesByWeek();

            } else if (dataPeriod.equalsIgnoreCase(DataPeriod.MONTH.toString())) {

                this.dataHandler.fetchCaloriesByMonth();

            }

        } else if (dataType.equalsIgnoreCase(DataType.DISTANCE.toString())) {

            if(dataPeriod.equalsIgnoreCase(DataPeriod.DAY.toString())) {

                this.dataHandler.fetchDistanceByDay();

            } else if (dataPeriod.equalsIgnoreCase(DataPeriod.WEEK.toString())) {

                this.dataHandler.fetchDistanceByWeek();

            } else if (dataPeriod.equalsIgnoreCase(DataPeriod.MONTH.toString())) {

                this.dataHandler.fetchDistanceByMonth();

            }

        } else if (dataType.equalsIgnoreCase(DataType.ACTIVITY_LEVEL.toString())) {

            if(dataPeriod.equalsIgnoreCase(DataPeriod.DAY.toString())) {

                this.dataHandler.fetchActivityLevelByDay();

            } else if (dataPeriod.equalsIgnoreCase(DataPeriod.WEEK.toString())) {

                this.dataHandler.fetchActivityLevelByWeek();

            } else if (dataPeriod.equalsIgnoreCase(DataPeriod.MONTH.toString())) {

                this.dataHandler.fetchActivityLevelByMonth();

            }

        }

        return null;
    }

    private <T,O> DataResponseDTO dtofy(Map<T, O> runningData, Map<T, O> walkingData) {

        List<String> series = new LinkedList<>();

        series.add("Running");
        series.add("Walking");

        List<String> runningDurations = new LinkedList<>();

        for(O o : runningData.values()) {
            runningDurations.add(o.toString());
        }

        List<String> walkingDurations = new LinkedList<>();

        for(O o : walkingData.values()) {
            walkingDurations.add(o.toString());
        }

        List<String[]> data = new LinkedList<>();

        data.add(runningDurations.toArray(new String[0]));
        data.add(walkingDurations.toArray(new String[0]));

        List<String> labels = new LinkedList<>();

        for(T t : runningData.keySet()){

            labels.add(t.toString());
        }

        return new DataResponseDTO(series, data, labels);

    }

    private LocalDateTime parseDate(String dateString) {
        return LocalDateTime.parse(dateString, DateTimeFormatter.ISO_DATE_TIME);
    }

}
