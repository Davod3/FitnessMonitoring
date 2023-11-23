package iot.project.processor.dtos;

public class DataRequestDTO {

    private String dataType;
    private String dataPeriod;
    private String start_date;
    private String end_date;

    public DataRequestDTO(String dataType, String dataPeriod, String start_date, String end_date) {
        this.dataType = dataType;
        this.dataPeriod = dataPeriod;
        this.start_date = start_date;
        this.end_date = end_date;
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
}
