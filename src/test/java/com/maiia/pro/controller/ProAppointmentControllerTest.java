package com.maiia.pro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maiia.pro.dto.AppointmentDto;
import com.maiia.pro.service.ProAppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProAppointmentController.class)
class ProAppointmentControllerTest {
    private final String END_POINT_PATH =  "/appointments";

    private AppointmentDto appointmentDto;

    @MockBean
    private ProAppointmentService proAppointmentService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;


    @BeforeEach
    void setUp(){
        appointmentDto = buildAppointment();
    }

    @Test
    void createAppointmentWithValidParamsReturnIsCreated() throws Exception {

        String jsonAppointment = mapper.writeValueAsString(appointmentDto);

        when(this.proAppointmentService.save(any())).thenReturn(appointmentDto);

        mockMvc.perform(MockMvcRequestBuilders.post(END_POINT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonAppointment))
                .andExpect(status().isCreated());
    }

    @Test
    void createAppointmentWithNullPatientIdReturnIsBadRequest() throws Exception {

        appointmentDto.setPatientId(null);
        String jsonAppointment = mapper.writeValueAsString(appointmentDto);

        mockMvc.perform(MockMvcRequestBuilders.post(END_POINT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonAppointment))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createAppointmentWithNullPractitionerIdReturnIsBadRequest() throws Exception {

        appointmentDto.setPractitionerId(null);
        String jsonAppointment = mapper.writeValueAsString(appointmentDto);

        mockMvc.perform(MockMvcRequestBuilders.post(END_POINT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonAppointment))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createAppointmentWithNegativePractitionerIdReturnIsBadRequest() throws Exception {

        appointmentDto.setPractitionerId(-5);
        String jsonAppointment = mapper.writeValueAsString(appointmentDto);

        mockMvc.perform(MockMvcRequestBuilders.post(END_POINT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonAppointment))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createAppointmentWithNullStartDateReturnIsBadRequest() throws Exception {

        appointmentDto.setStartDate(null);
        String jsonAppointment = mapper.writeValueAsString(appointmentDto);

        mockMvc.perform(MockMvcRequestBuilders.post(END_POINT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonAppointment))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createAppointmentWithNullEndDateReturnIsBadRequest() throws Exception {

        appointmentDto.setEndDate(null);
        String jsonAppointment = mapper.writeValueAsString(appointmentDto);

        mockMvc.perform(MockMvcRequestBuilders.post(END_POINT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonAppointment))
                .andExpect(status().isBadRequest());
    }

    private AppointmentDto buildAppointment() {

        return AppointmentDto.builder()
                .patientId(Integer.MAX_VALUE)
                .practitionerId(Integer.MAX_VALUE)
                .startDate(LocalDateTime.now().plusDays(1))
                .endDate(LocalDateTime.now().plusDays(2))
                .build();

    }
}