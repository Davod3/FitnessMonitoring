package iot.project.processor.handlers;

import iot.project.processor.entities.ProcessedUserData;
import iot.project.processor.entities.UserData;
import iot.project.processor.repositories.SavedUserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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
    private static final String CLASSIFIER_API = "http://172.100.10.17:3000/predict";
    private static final int INACTIVITY_THRESHOLD = 60;

    @Autowired
    private SavedUserDataRepository userDataRepo;
    @Autowired
    private MongoTemplate mongoTemplate;

    public void initializeTrainingDataset() throws IOException {

        System.out.println("Loading training data...");

        //Check if training data is initialized
        if(!mongoTemplate.collectionExists(USER_DATA)) {

            File file = new File(TRAINING_DATA);

            Files.lines(file.toPath()).forEach(this::addTrainingData);

            System.out.println("Pre-Processing training data...");

            Sort order = Sort.by(Sort.Direction.ASC, "dateTime");
            Query query = new Query().with(order);

            this.mongoTemplate.find(query, UserData.class).stream().forEach(this::computeAndSave);
        }

        System.out.println("Training data loaded!");

    }

    public void addTrainingData(String line) {

        if(!line.contains("date")) {
            //Skip header line
            try {
                UserData data = dataParser(line);
                this.mongoTemplate.save(data, RAW_USER_DATA);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }


        }

    }

    private void addOnlineData(String cleanData) {
    }

    private void computeAndSave(UserData data) {

        ProcessedUserData savedData = this.userDataRepo.findByDate(data.getDateTime().toLocalDate());

        if(savedData != null) {

            long oldTimestamp = savedData.getLastTimestamp();
            long newTimestamp = data.getDateTime().atZone(ZoneId.systemDefault()).toEpochSecond();
            long activityDuration = newTimestamp - oldTimestamp;

            savedData.setLastTimestamp(newTimestamp);

            //Check for inactivity
            if(activityDuration < INACTIVITY_THRESHOLD) {

                if(data.getActivity() == 0) {

                    //Walking
                    long oldWalkingDuration = savedData.getWalkingDuration();
                    savedData.setWalkingDuration(oldWalkingDuration + activityDuration);

                } else {

                    //Running
                    long oldRunningDuration = savedData.getRunningDuration();
                    savedData.setRunningDuration(oldRunningDuration + activityDuration);
                }

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

    public void handleIncomingData(String receivedMessage) {

        try {

            String classifiedData = classify(receivedMessage);

            String cleanData = cleanData(classifiedData);

            addOnlineData(cleanData);



        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

    private String cleanData(String classifiedData) {

        StringBuilder sb = new StringBuilder();

        String firstClean = classifiedData.replace("{", "");
        String secondClean = firstClean.replace("}", "");
        String thirdClean = secondClean.replace(" ", "");
        String fourthClean = thirdClean.replace("\"", "");

        String[] fields = fourthClean.split(",");

        for(String field : fields) {

            String[] splitField = field.split(":");

            if(splitField[0].contains("time")) {
                String time = splitField[1].replace("-", ":");
                sb.append(time).append(";");
            } else {

                sb.append(splitField[1]).append(";");

            }

        }

        return sb.toString();

    }

    private String classify(String receivedMessage) throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(CLASSIFIER_API))
                .POST(HttpRequest.BodyPublishers.ofString(receivedMessage))
                .header("Content-Type", "application/json")
                .build();

        String response = client.send(request, HttpResponse.BodyHandlers.ofString()).body();

        return response;

    }

    public int currentActivity() {

        /*
            Code based on https://tecadmin.net/mongodb-get-the-last-record-from-collection/
         */

        Query query = new Query().limit(1).with(Sort.by(Sort.Order.desc("$natural")));
        UserData data = this.mongoTemplate.findOne(query, UserData.class);

        Long lastTimestamp = data.getDateTime().atZone(ZoneId.systemDefault()).toEpochSecond();
        Long now = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond();

        if(now - lastTimestamp > INACTIVITY_THRESHOLD) {
            //Inactivity Threshold
            return -1;
        }

        return data.getActivity();

    }
}
