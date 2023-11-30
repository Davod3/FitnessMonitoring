package iot.project.processor.handlers;

import iot.project.processor.dtos.DataResponse;
import iot.project.processor.utils.VelocityCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Component
public class DistanceHandler {

    @Autowired
    private DataHandler dataHandler;

    public DataResponse<String, Double> fetchDistanceByDay(LocalDate startDate, LocalDate endDate, int age,
                                                           String gender) {

        DataResponse<String, Long> response = this.dataHandler.fetchDurationByDay(startDate, endDate);

        return fetchDistance(response, age, gender);
    }

    public DataResponse<String, Double> fetchDistanceByWeek(LocalDate startDate, LocalDate endDate, int age,
                                                            String gender) {

        DataResponse<String, Long> response = this.dataHandler.fetchDurationByWeek(startDate, endDate);

        return fetchDistance(response, age, gender);
    }

    public DataResponse<String, Double> fetchDistanceByMonth(LocalDate startDate, LocalDate endDate, int age,
                                                             String gender) {

        DataResponse<String, Long> response = this.dataHandler.fetchDurationByMonth(startDate, endDate);

        return fetchDistance(response, age, gender);
    }

    private DataResponse<String, Double> fetchDistance(DataResponse<String, Long> response, int age,
                                                       String gender) {


        List<Double> distanceWalking = new LinkedList<>();
        List<Double> distanceRunning = new LinkedList<>();

        double velocityRunning = VelocityCalculator.getAverageVelocity(age, gender, true);
        double velocityWalking = VelocityCalculator.getAverageVelocity(age, gender, false);

        for(long duration : response.getInformationRunning()) {

            distanceRunning.add(calculateDistanceMeters(velocityRunning, duration));

        }

        for(long duration : response.getInformationWalking()) {
            distanceWalking.add(calculateDistanceMeters(velocityWalking, duration));
        }

        return new DataResponse<String, Double>(response.getDates(), distanceRunning, distanceWalking);

    }

    private double calculateDistanceMeters(double velocity, long durationInSeconds) {
        return velocity * durationInSeconds;
    }

}
