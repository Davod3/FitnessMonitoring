package iot.project.processor.dtos;

import java.util.List;
import java.util.Map;

public class DataResponse<T, O> {

   private List<T> dates;
   private List<O> informationWalking;
   private List<O> informationRunning;

    public DataResponse(List<T> dates, List<O> informationRunning, List<O> informationWalking) {

        this.dates = dates;
        this.informationWalking = informationWalking;
        this.informationRunning = informationRunning;

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
}
