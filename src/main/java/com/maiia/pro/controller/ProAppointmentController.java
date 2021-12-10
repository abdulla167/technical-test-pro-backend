package com.maiia.pro.controller;

import com.maiia.pro.dto.AppointmentDto;
import com.maiia.pro.entity.Appointment;
import com.maiia.pro.exception.ApiException;
import com.maiia.pro.service.AppointmentService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

    @ApiOperation(value = "Get all the appointments of a practitioner by practitioner id", response = List.class)
    @GetMapping("/{practitionerId}")
    public List<Appointment> getAppointmentsByPractitioner(@PathVariable final String practitionerId) {
        return appointmentService.findByPractitionerId(practitionerId);
    }

    @ApiOperation(value = "Get all the appointments of all practitioners", response = List.class)
    @GetMapping
    public List<Appointment> getAppointments() {
        return appointmentService.findAll();
    }

    @ApiOperation(value = "Create a new appointment between patient and practitioner")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Appointment has created successfully", response = AppointmentDto.class),
            @ApiResponse(code = 400, message = "There is a problem in one of the parameter in request body", response = ApiException.class),
            })
    @PostMapping
    public ResponseEntity<Object> createAppointment(@Valid @RequestBody final AppointmentDto appointmentDto) {
        AppointmentDto responseBody = this.appointmentService.save(appointmentDto);

        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }
}
