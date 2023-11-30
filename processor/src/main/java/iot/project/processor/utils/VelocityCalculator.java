package iot.project.processor.utils;

public class VelocityCalculator {

    private static final String MALE = "male";
    private static final String FEMALE = "female";

    public static double getAverageVelocity(int age, String gender, boolean isRunning){

        double result;

        if(age >= 20 && age <= 29) {

            result = gender.equals(MALE)?1.36:1.34;

        } else if (age >= 30 && age <= 39) {

            result = gender.equals(MALE)?1.43:1.34;

        } else if (age >= 40 && age <= 49) {

            result = gender.equals(MALE)?1.43:1.39;

        } else if (age >= 50 && age <= 59) {

            result = gender.equals(MALE)?1.43:1.31;

        } else if (age >= 60 && age <= 69) {

            result = gender.equals(MALE)?1.34:1.24;

        } else if(age >= 70 && age <= 79) {

            result = gender.equals(MALE)?1.26:1.13;

        } else {

            result = gender.equals(MALE)?0.97:0.94;

        }

        return isRunning?2*result:result;


    }

}
