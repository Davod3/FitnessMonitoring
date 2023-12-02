package iot.project.processor.controllers;

import iot.project.processor.dtos.DataRequestDTO;
import iot.project.processor.dtos.DataResponseDTO;
import iot.project.processor.services.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
public class DataController {

    @Autowired private DataService dataService;
    @PostMapping("/duration")
    public DataResponseDTO fetchDuration(@RequestBody DataRequestDTO request) {
        return dataService.fetchDuration(request);
    }

    @PostMapping("/calories")
    public DataResponseDTO fetchCalories(@RequestBody DataRequestDTO request) {
        return dataService.fetchCalories(request);
    }

    @PostMapping("/distance")
    public DataResponseDTO fetchDistance(@RequestBody DataRequestDTO request) {
        return dataService.fetchDistance(request);
    }

    @PostMapping("/activitylevel")
    public DataResponseDTO fetchActivityLevel(@RequestBody DataRequestDTO request) {
        return dataService.fetchActivityLevel(request);
    }
}
