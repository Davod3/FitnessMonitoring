package iot.project.processor.handlers;

import iot.project.processor.documents.UserData;
import iot.project.processor.dtos.DataResponse;
import iot.project.processor.dtos.DataResponseDTO;
import iot.project.processor.repositories.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class DataHandler {

    private static final String TRAINING_DATA = "training.data";
    private static final String USER_DATA = "user_data";

    @Autowired
    private UserDataRepository userDataRepo;

    @Autowired
    private MongoTemplate mongoTemplate;

    public void initializeTrainingDataset() throws IOException {


        //Check if training data is initialized

        if(!mongoTemplate.collectionExists(USER_DATA)) {

            File file = new File(TRAINING_DATA);

            Files.lines(file.toPath()).forEach(this::addTrainingData);

        }

    }

    public void addTrainingData(String line) {

        if(!line.contains("date")) {
            //Skip header line
            try {
                UserData data = dataParser(line);
                this.mongoTemplate.save(data, USER_DATA);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }


        }

    }

    private UserData dataParser(String line) throws ParseException {

        String[] splitData = line.split(";");

        UserData data = new UserData();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:m:s:n");

        Date d = sdf.parse(splitData[0]);
        LocalDate ld = LocalDate.ofInstant(d.toInstant(), ZoneId.systemDefault());
        LocalTime lt = LocalTime.parse(splitData[1], dtf);
        data.setDateTime(LocalDateTime.of(ld,lt));
        data.setActivity(Integer.parseInt(splitData[2]));
        data.setAccelerationX(Double.parseDouble(splitData[3]));
        data.setAccelerationY(Double.parseDouble(splitData[4]));
        data.setAccelerationZ(Double.parseDouble(splitData[5]));
        data.setGyroX(Double.parseDouble(splitData[6]));
        data.setGyroY(Double.parseDouble(splitData[7]));
        data.setGyroZ(Double.parseDouble(splitData[8]));

        return data;

    }

    public DataResponse<LocalDate, Long> fetchDurationByDay(LocalDateTime startDate, LocalDateTime endDate) {

        Map<LocalDate, Long> durationPerDayWalking = new HashMap<>();
        Map<LocalDate, Long> durationPerDayRunning = new HashMap<>();

        Map<LocalDate, Long> previousTimestampPerDay = new HashMap<>();

        //Get all data from db between these 2 days
        this.userDataRepo.findBetweenStartEnd(startDate, endDate).stream().forEach( data -> {

            LocalDateTime dateTime = data.getDateTime();
            LocalDate date = dateTime.toLocalDate();

            if(!previousTimestampPerDay.containsKey(date)){

                //If date is found for first time:
                durationPerDayWalking.put(date, 0L);
                durationPerDayRunning.put(date, 0L);
                previousTimestampPerDay.put(date, dateTime.atZone(ZoneId.systemDefault()).toEpochSecond());

            } else {

                //If date is already found:
                Long currentTimestamp = dateTime.atZone(ZoneId.systemDefault()).toEpochSecond();
                Long storedTimestamp = previousTimestampPerDay.get(date);

                Long duration = currentTimestamp - storedTimestamp;

                if(data.getActivity() == 0) {
                    //Walking

                    Long storedDuration = durationPerDayWalking.get(date);
                    durationPerDayWalking.replace(date, storedDuration + duration);

                } else {

                    //Running
                    Long storedDuration = durationPerDayRunning.get(date);
                    durationPerDayRunning.replace(date, storedDuration + duration);

                }

                previousTimestampPerDay.replace(date, currentTimestamp);

            }

        });

        System.out.println(Arrays.toString(durationPerDayRunning.keySet().toArray()));

        return new DataResponse<LocalDate, Long>(durationPerDayRunning, durationPerDayWalking);

    }

    public void fetchDurationByWeek() {
    }

    public DataResponse<String, Long> fetchDurationByMonth(LocalDateTime startDate, LocalDateTime endDate) {

        Map<String, Long> durationPerDayWalking = new HashMap<>();
        Map<String, Long> durationPerDayRunning = new HashMap<>();

        List<Month> monthsBetween = getMonthsBetween(startDate, endDate);
        LocalDateTime start = startDate;
        LocalDateTime end;

        for(Month m : monthsBetween) {

            YearMonth yearMonth = YearMonth.of(start.getYear(), m.getValue());
            LocalDate endOfMonth = yearMonth.atEndOfMonth();

            if(endOfMonth.atStartOfDay().isAfter(endDate)) {
                end = endDate;
            } else {
                end = endOfMonth.atStartOfDay();
            }

            System.out.println("Fetching " + m.toString() + ": " + start.toString() + " - " + end.toString());

            DataResponse<LocalDate, Long> durationPerDay = fetchDurationByDay(start, end);

            long totalRunning = 0;

            for(long l : durationPerDay.getRunningData().values()) {
                totalRunning+=l;
            }

            long totalWalking = 0;

            for(long l : durationPerDay.getWalkingData().values()) {
                totalWalking+=l;
            }

            durationPerDayRunning.put(m.toString(), totalRunning);

            durationPerDayWalking.put(m.toString(),totalWalking);

            start = endOfMonth.plusDays(1).atStartOfDay();

        }

        return new DataResponse<String, Long>(durationPerDayRunning, durationPerDayWalking);

    }

    public void fetchCaloriesByDay() {
    }

    public void fetchCaloriesByWeek() {
    }

    public void fetchCaloriesByMonth() {
    }

    public void fetchDistanceByDay() {
    }

    public void fetchDistanceByWeek() {
    }

    public void fetchDistanceByMonth() {
    }

    public void fetchActivityLevelByDay() {
    }

    public void fetchActivityLevelByWeek() {
    }

    public void fetchActivityLevelByMonth() {
    }

    private static List<Month> getMonthsBetween(LocalDateTime startDate, LocalDateTime endDate) {
        List<Month> monthsBetween = new ArrayList<>();
        LocalDateTime currentMonth = startDate;

        while (!currentMonth.isAfter(endDate)) {
            monthsBetween.add(currentMonth.getMonth());
            currentMonth = currentMonth.plusMonths(1);
        }

        return monthsBetween;
    }
}
