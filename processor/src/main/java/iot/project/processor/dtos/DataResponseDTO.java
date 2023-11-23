package iot.project.processor.dtos;

import java.util.Map;

public class DataResponseDTO {

    private Map<String, String > results;

    public DataResponseDTO(Map<String, String> results) {
        this.results = results;
    }

    public Map<String, String> getResults() {
        return results;
    }

    public void setResults(Map<String, String> results) {
        this.results = results;
    }
}
