package iot.project.processor.handlers;

import iot.project.processor.dtos.DataResponse;
import iot.project.processor.utils.VelocityCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Component
public class CaloriesHandler {

    @Autowired
    private DataHandler dataHandler;
    private final double CONSTANT_1 = 0.035;
    private final double CONSTANT_2 = 0.029;

    public DataResponse<String, Double> fetchCaloriesByDay(LocalDate startDate, LocalDate endDate, int age,
                                                              double height, double weight, String gender) {

        DataResponse<String, Long> response = this.dataHandler.fetchDurationByDay(startDate, endDate);

        return fetchCalories(response, age, height, weight, gender);


    }

    public DataResponse<String, Double> fetchCaloriesByWeek(LocalDate startDate, LocalDate endDate, int age,
                                                               double height, double weight, String gender) {

        DataResponse<String, Long> response = this.dataHandler.fetchDurationByWeek(startDate, endDate);

        return fetchCalories(response, age, height, weight, gender);

    }

    public DataResponse<String, Double> fetchCaloriesByMonth(LocalDate startDate, LocalDate endDate, int age,
                                                                double height, double weight, String gender) {

        DataResponse<String, Long> response = this.dataHandler.fetchDurationByMonth(startDate, endDate);

        return fetchCalories(response, age, height, weight, gender);
    }

    private DataResponse<String, Double> fetchCalories(DataResponse<String, Long> response, int age,
                                                                 double height, double weight, String gender) {

        List<Double> caloriesWalking = new LinkedList<>();
        List<Double> caloriesRunning = new LinkedList<>();

        double heightInMeters = height / 100;
        double avgCaloriesWalking = computeCalories(age, heightInMeters, weight, gender, false);
        double avgCaloriesRunning = computeCalories(age, heightInMeters, weight, gender, true);

        for(long o : response.getInformationWalking()) {

            double durationInMinutes = o / 60.0;

            caloriesWalking.add(durationInMinutes * avgCaloriesWalking);

        }

        for(long l : response.getInformationRunning()) {

            double durationInMinutes = l / 60.0;

            caloriesRunning.add(durationInMinutes * avgCaloriesRunning);
        }

        return new DataResponse<String, Double>(response.getDates(), caloriesRunning, caloriesWalking);

    }

    private double computeCalories(int age, double height, double weight, String gender, boolean isRunning) {

        double velocity = VelocityCalculator.getAverageVelocity(age, gender, isRunning);

        return (CONSTANT_1 * weight) + ((Math.pow(velocity, 2) / height) * CONSTANT_2 * weight);

    }

}
