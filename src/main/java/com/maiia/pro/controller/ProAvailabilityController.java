package com.maiia.pro.controller;

import com.maiia.pro.dto.AvailabilityDto;
import com.maiia.pro.service.AvailabilityService;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/availabilities", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProAvailabilityController {

    private final AvailabilityService availabilityService;

    @Autowired
    public ProAvailabilityController(AvailabilityService availabilityService1){
        this.availabilityService = availabilityService1;
    }

    @ApiOperation(value = "Get availabilities by practitionerId", response = List.class)
    @GetMapping
    public List<AvailabilityDto> getAvailabilities(@RequestParam final Integer practitionerId) {
        return availabilityService.findByPractitionerId(practitionerId);
    }
}
