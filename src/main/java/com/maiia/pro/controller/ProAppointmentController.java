package com.maiia.pro.controller;

import com.maiia.pro.dto.AppointmentDto;
import com.maiia.pro.entity.Appointment;
import com.maiia.pro.response.ResponseHandler;
import com.maiia.pro.service.AppointmentService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.maiia.pro.template.AppointmentResponseMessage.APPOINTMENT_CREATED;

import javax.validation.Valid;
import java.util.List;


@CrossOrigin
@RestController
@RequestMapping(value = "/appointments", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProAppointmentController {

    private final AppointmentService appointmentService;

    @Autowired
    ProAppointmentController(AppointmentService appointmentService){
        this.appointmentService = appointmentService;
    }

    @ApiOperation(value = "Get appointments by practitionerId")
    @GetMapping("/{practitionerId}")
    public List<Appointment> getAppointmentsByPractitioner(@PathVariable final String practitionerId) {
        return appointmentService.findByPractitionerId(practitionerId);
    }

    @ApiOperation(value = "Get all appointments")
    @GetMapping
    public List<Appointment> getAppointments() {
        return appointmentService.findAll();
    }

    @ApiOperation(value = "Create a new appointment")
    @PostMapping
    public ResponseEntity<Object> createAppointment(@Valid @RequestBody final AppointmentDto appointmentDto) {
        AppointmentDto responseBody = this.appointmentService.save(appointmentDto);

        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }
}
