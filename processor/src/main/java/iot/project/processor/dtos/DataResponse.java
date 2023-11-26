package iot.project.processor.dtos;

import java.util.Map;

public class DataResponse<T, O> {

    private Map<T, O> runningData;
    private Map<T,O> walkingData;

    public DataResponse(Map<T, O> runningData, Map<T, O> walkingData) {
        this.runningData = runningData;
        this.walkingData = walkingData;
    }

    public Map<T, O> getRunningData() {
        return runningData;
    }

    public Map<T, O> getWalkingData() {
        return walkingData;
    }
}
