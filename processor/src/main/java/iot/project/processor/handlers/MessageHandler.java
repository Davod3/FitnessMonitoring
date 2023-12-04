package iot.project.processor.handlers;

import iot.project.processor.entities.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;

public class MessageHandler implements org.springframework.messaging.MessageHandler {

    @Autowired
    private DataHandler dataHandler;
    private final String EOL = System.lineSeparator();

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {

        String receivedMessage = message.getPayload().toString();

        String cleanMessage = cleanMessage(receivedMessage);


        /*

        try {
            UserData data = parseData(receivedMessage);


        } catch (ParseException e) {
            System.out.println("Failed to parse received data!");
        }

         */

    }

    private String cleanMessage(String receivedMessage) {

        System.out.println(receivedMessage);

        return null;

    }

    private UserData parseData(String line) throws ParseException {

        UserData data = new UserData();

        String firstClean = line.replace("{", "");
        String secondClean = firstClean.replace("}", "");
        String finalString = secondClean.replace(" ", "");

        String[] splitData = finalString.split(",");

        String[] fields = new String[splitData.length];

        for(int i = 0; i < splitData.length; i++) {

            String[] splitField = splitData[i].split(":");
            fields[i] = splitField[1];

        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH-m-s-n");

        Date d = sdf.parse(fields[0]);
        LocalDate ld = LocalDate.ofInstant(d.toInstant(), ZoneId.systemDefault());
        LocalTime lt = LocalTime.parse(fields[1], dtf);
        data.setDateTime(LocalDateTime.of(ld,lt));
        data.setAccelerationX(Double.parseDouble(fields[2]));
        data.setAccelerationY(Double.parseDouble(fields[3]));
        data.setAccelerationZ(Double.parseDouble(fields[4]));
        data.setGyroX(Double.parseDouble(fields[5]));
        data.setGyroY(Double.parseDouble(fields[6]));
        data.setGyroZ(Double.parseDouble(fields[7]));

        return data;
    }
}
