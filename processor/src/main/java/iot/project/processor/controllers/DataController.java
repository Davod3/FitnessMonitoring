package iot.project.processor.controllers;

import iot.project.processor.dtos.DataRequestDTO;
import iot.project.processor.dtos.DataResponseDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class DataController {

    @PostMapping("/data")
    public DataResponseDTO fetchData(@RequestBody DataRequestDTO request) {

        Map<String, String> test = new HashMap<>();

        test.put("Janeiro", "40");
        test.put("Março", "10");

        DataResponseDTO response = new DataResponseDTO(test);

        return response;

    }

}
