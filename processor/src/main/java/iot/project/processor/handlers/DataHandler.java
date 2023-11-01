package iot.project.processor.handlers;

import iot.project.processor.documents.UserData;
import iot.project.processor.repositories.UserDataRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;

@Component
public class DataHandler {

    private static final String TRAINING_DATA = "training.data";

    @Autowired
    private UserDataRepository userDataRepo;

    public void initializeTrainingDataset() throws IOException {

        File file = new File(TRAINING_DATA);

        Files.lines(file.toPath()).forEach(this::addData);

    }

    public void addData(String line) {

        if(!line.contains("date")) {
            //Skip header line

            String[] splitData = line.split(";");

            UserData data = new UserData();

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:m:s:n");

            try {
                Date d = sdf.parse(splitData[0]);
                data.setDate(LocalDate.ofInstant(d.toInstant(), ZoneId.systemDefault()));
                data.setTime(LocalTime.parse(splitData[1], dtf));
                data.setActivity(Integer.parseInt(splitData[2]));
                data.setAccelerationX(Double.parseDouble(splitData[3]));
                data.setAccelerationY(Double.parseDouble(splitData[4]));
                data.setAccelerationZ(Double.parseDouble(splitData[5]));
                data.setGyroX(Double.parseDouble(splitData[6]));
                data.setGyroY(Double.parseDouble(splitData[7]));
                data.setGyroZ(Double.parseDouble(splitData[8]));

                this.userDataRepo.save(data);

            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

        }

    }

}
