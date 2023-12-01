package iot.project.processor.dtos;

import java.util.List;
import java.util.Map;

public class DataResponse<T, O> {

   private List<T> dates;
   private List<O> informationWalking;
   private List<O> informationRunning;
   private List<String> series;

    public DataResponse(List<T> dates, List<O> informationRunning, List<O> informationWalking, List<String> series) {

        this.dates = dates;
        this.informationWalking = informationWalking;
        this.informationRunning = informationRunning;
        this.series = series;

    }

    public List<T> getDates() {
        return dates;
    }

    public List<O> getInformationWalking() {
        return informationWalking;
    }

    public List<O> getInformationRunning() {
        return informationRunning;
    }

    public List<String> getSeries() {
        return series;
    }
}
