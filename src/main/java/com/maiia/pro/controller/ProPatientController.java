package com.maiia.pro.controller;

import com.maiia.pro.entity.Patient;
import com.maiia.pro.service.PatientService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/patients", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProPatientController {

    private final PatientService patientService;

    @Autowired
    public ProPatientController(PatientService patientService1){
        this.patientService = patientService1;
    }

    @ApiOperation(value = "Get patients", response = List.class)
    @GetMapping
    public List<Patient> getPatients() {
        return patientService.findAll();
    }
}
