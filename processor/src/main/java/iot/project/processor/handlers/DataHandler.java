package iot.project.processor.handlers;

import iot.project.processor.entities.ProcessedUserData;
import iot.project.processor.entities.UserData;
import iot.project.processor.dtos.DataResponse;
import iot.project.processor.entities.Week;
import iot.project.processor.repositories.SavedUserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class DataHandler {

    private static final String TRAINING_DATA = "training.data";
    private static final String USER_DATA = "processed_user_data";
    private static final String RAW_USER_DATA = "raw_user_data";

    @Autowired
    private SavedUserDataRepository userDataRepo;

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
                this.mongoTemplate.save(data, RAW_USER_DATA);
                computeAndSave(data);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }


        }

    }

    private void computeAndSave(UserData data) {

        ProcessedUserData savedData = this.userDataRepo.findByDate(data.getDateTime().toLocalDate());

        if(savedData != null) {

            long oldTimestamp = savedData.getLastTimestamp();
            long newTimestamp = data.getDateTime().atZone(ZoneId.systemDefault()).toEpochSecond();
            long activityDuration = newTimestamp - oldTimestamp;

            savedData.setLastTimestamp(newTimestamp);

            if(data.getActivity() == 0) {

                //Walking
                long oldWalkingDuration = savedData.getWalkingDuration();
                savedData.setWalkingDuration(oldWalkingDuration + activityDuration);

            } else {

                //Running
                long oldRunningDuration = savedData.getRunningDuration();
                savedData.setRunningDuration(oldRunningDuration + activityDuration);
            }


        } else {

            //Date found for the first time
            savedData = new ProcessedUserData();
            savedData.setDate(data.getDateTime().toLocalDate());
            savedData.setRunningDuration(0);
            savedData.setWalkingDuration(0);
            savedData.setLastTimestamp(data.getDateTime().atZone(ZoneId.systemDefault()).toEpochSecond());

        }

        this.userDataRepo.save(savedData);


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

    public DataResponse<LocalDate, Long> fetchDurationByDay(LocalDate startDate, LocalDate endDate) {

        List<LocalDate> dates = new LinkedList<>();

        List<Long> durationsRunning = new LinkedList<>();
        List<Long> durationsWalking = new LinkedList<>();


        //Get all data from db between these 2 days
        this.userDataRepo.findBetweenStartEnd(startDate, endDate).stream().forEach( data -> {

            dates.add(data.getDate());

            durationsRunning.add(data.getRunningDuration());
            durationsWalking.add(data.getWalkingDuration());

        });


        return new DataResponse<LocalDate, Long>(dates, durationsRunning, durationsWalking);

    }

    public DataResponse<LocalDate, Long> fetchDurationByWeek(LocalDate startDate, LocalDate endDate) {

        List<LocalDate> dates = new LinkedList<>();

        List<Long> durationWalking = new LinkedList<>();
        List<Long> durationRunning = new LinkedList<>();

        List<Week> weeks = getWeeksBetween(startDate, endDate);

        for (Week w : weeks) {

            DataResponse<LocalDate, Long> response = fetchDurationByDay(w.getStart(), w.getEnd());

            dates.add(w.getStart());

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

        return new DataResponse<LocalDate, Long>(dates, durationRunning, durationWalking);
        
    }

    public DataResponse<String, Long> fetchDurationByMonth(LocalDate startDate, LocalDate endDate) {

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

            DataResponse<LocalDate, Long> durationPerDay = fetchDurationByDay(start, end);

            long totalRunning = 0;

            for(long l : durationPerDay.getInformationRunning()) {
                totalRunning+=l;
            }

            long totalWalking = 0;

            for(long l : durationPerDay.getInformationWalking()) {
                totalWalking+=l;
            }

            dates.add(m.toString());

            durationRunning.add(totalRunning);

            durationWalking.add(totalWalking);

            start = endOfMonth.plusDays(1);

        }

        return new DataResponse<String, Long>(dates, durationRunning, durationWalking);

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

    private static List<Month> getMonthsBetween(LocalDate startDate, LocalDate endDate) {
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
