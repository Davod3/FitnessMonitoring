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

    @PostMapping("/data")
    public DataResponseDTO fetchData(@RequestBody DataRequestDTO request) {
        return dataService.fetchData(request);
    }

}
