package iot.project.processor.services;

import iot.project.processor.dtos.DataPeriod;
import iot.project.processor.dtos.DataRequestDTO;
import iot.project.processor.dtos.DataResponseDTO;
import iot.project.processor.dtos.DataType;
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

                    this.dataHandler.fetchDurationByDay(parsedStartDate, parsedEndDate);

                } else if (dataPeriod.equalsIgnoreCase(DataPeriod.WEEK.toString())) {

                    this.dataHandler.fetchDurationByWeek();

                } else if (dataPeriod.equalsIgnoreCase(DataPeriod.MONTH.toString())) {

                    this.dataHandler.fetchDurationByMonth();

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


        String[] data1 = {"5", "6"};
        String[] data2 = {"8", "2"};

        List<String> series = new LinkedList<>();

        series.add("Running");
        series.add("Walking");

        List<String[]> data = new LinkedList<>();

        data.add(data1);
        data.add(data2);

        List<String> labels = new LinkedList<>();

        labels.add("Janeiro");
        labels.add("Fevereiro");

        DataResponseDTO response = new DataResponseDTO(series, data, labels);

        return response;
    }

    private LocalDateTime parseDate(String dateString) {
        return LocalDateTime.parse(dateString, DateTimeFormatter.ISO_DATE_TIME);
    }

}
