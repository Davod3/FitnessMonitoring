package iot.project.processor.dtos;

import java.util.List;
import java.util.Map;

public class DataResponseDTO {

    private List<String> series;
    private List<String[]> data;
    private List<String> labels;

    public DataResponseDTO(List<String> series, List<String[]> data, List<String> labels) {
        this.series = series;
        this.data = data;
        this.labels = labels;
    }

    public List<String> getSeries() {
        return series;
    }

    public void setSeries(List<String> series) {
        this.series = series;
    }

    public List<String[]> getData() {
        return data;
    }

    public void setData(List<String[]> data) {
        this.data = data;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }
}
