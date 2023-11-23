package iot.project.processor.controllers;

import iot.project.processor.dtos.DataRequestDTO;
import iot.project.processor.dtos.DataResponseDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
public class DataController {

    @PostMapping("/data")
    public DataResponseDTO fetchData(@RequestBody DataRequestDTO request) {

        String[] data1 = {"5", "6"};
        String[] data2 = {"8", "2"};
        String[] data3 = {"6", "4"};

        List<String> series = new LinkedList<>();

        series.add("Running");
        series.add("Walking");

        List<String[]> data = new LinkedList<>();

        data.add(data1);
        data.add(data2);
        data.add(data3);

        List<String> labels = new LinkedList<>();

        labels.add("Janeiro");
        labels.add("Fevereiro");
        labels.add("Março");

        DataResponseDTO response = new DataResponseDTO(series, data, labels);

        return response;

    }

}
