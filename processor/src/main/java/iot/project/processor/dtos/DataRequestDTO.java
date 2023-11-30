package iot.project.processor.dtos;

public class DataRequestDTO {

    private String dataType;
    private String dataPeriod;
    private String start_date;
    private String end_date;
    private int age;
    private double height;
    private double weight;
    private String gender;

    public DataRequestDTO(String dataType, String dataPeriod, String start_date, String end_date, int age,
                          double height, double weight, String gender) {
        this.dataType = dataType;
        this.dataPeriod = dataPeriod;
        this.start_date = start_date;
        this.end_date = end_date;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDataPeriod() {
        return dataPeriod;
    }

    public void setDataPeriod(String dataPeriod) {
        this.dataPeriod = dataPeriod;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
