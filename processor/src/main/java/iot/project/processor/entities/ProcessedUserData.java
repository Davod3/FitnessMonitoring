package iot.project.processor.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document("processed_user_data")
public class ProcessedUserData {

    @Id
    private String id;
    private LocalDate date;
    private long lastTimestamp; //Seconds
    private long runningDuration; //Seconds
    private long walkingDuration; //Seconds

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public long getLastTimestamp() {
        return lastTimestamp;
    }

    public void setLastTimestamp(long lastTimestamp) {
        this.lastTimestamp = lastTimestamp;
    }

    public long getRunningDuration() {
        return runningDuration;
    }

    public void setRunningDuration(long runningDuration) {
        this.runningDuration = runningDuration;
    }

    public long getWalkingDuration() {
        return walkingDuration;
    }

    public void setWalkingDuration(long walkingDuration) {
        this.walkingDuration = walkingDuration;
    }
}
