package iot.project.processor.handlers;

import iot.project.processor.documents.UserData;
import iot.project.processor.repositories.UserDataRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Component
public class DataHandler {

    private static final String TRAINING_DATA = "training.data";
    private static final String TRAINING_COLLECTION = "training_data";

    @Autowired
    private UserDataRepository userDataRepo;

    @Autowired
    private MongoTemplate mongoTemplate;

    public void initializeTrainingDataset() throws IOException {


        //Check if training data is initialized

        if(!mongoTemplate.collectionExists(TRAINING_COLLECTION)) {

            File file = new File(TRAINING_DATA);

            Files.lines(file.toPath()).forEach(this::addTrainingData);

        }

    }

    public void addTrainingData(String line) {

        if(!line.contains("date")) {
            //Skip header line
            try {
                UserData data = dataParser(line);
                this.mongoTemplate.save(data, TRAINING_COLLECTION);
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

    public void fetchDurationByDay() {
    }

    public void fetchDurationByWeek() {
    }

    public void fetchDurationByMonth() {
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
}
