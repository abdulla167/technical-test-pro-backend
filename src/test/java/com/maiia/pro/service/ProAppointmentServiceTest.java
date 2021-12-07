package com.maiia.pro.service;

import com.maiia.pro.dto.AppointmentDto;
import com.maiia.pro.entity.Appointment;
import com.maiia.pro.entity.Patient;
import com.maiia.pro.entity.Practitioner;
import com.maiia.pro.exception.ApiRequestException;
import com.maiia.pro.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProAppointmentServiceTest {

    private final Integer DUMMY_ID = 2;

    @Mock
    AppointmentRepository appointmentRepository;

    @Mock
    PatientRepository patientRepository;

    @Mock
    PractitionerRepository practitionerRepository;

    @Mock
    TimeSlotRepository timeSlotRepository;

    @Mock
    AvailabilityRepository availabilityRepository;

    @Mock
    ProAvailabilityService availabilityService;

    @InjectMocks
    ProAppointmentService proAppointmentService;

    ProAppointmentServiceTest() {}

    @Test
    @DisplayName("Checking if end date is after the start date")
    void checkIfEndDateIsAfterStartDate(){
        LocalDateTime endDate = LocalDateTime.of(2020, Month.FEBRUARY, 5, 11, 0, 0);

        AppointmentDto appointmentDto = AppointmentDto.builder()
                .id(DUMMY_ID)
                .startDate(endDate)
                .endDate(endDate.minusHours(1)).build();

        assertThrows(ApiRequestException.class, () -> this.proAppointmentService.save(appointmentDto));
    }

    @Test
    @DisplayName("Checking The appointment has no real practitioner id")
    void checkIfPractitionerNotExist(){
        LocalDateTime startDate = LocalDateTime.of(2020, Month.FEBRUARY, 5, 11, 0, 0);
        AppointmentDto appointmentDto = AppointmentDto.builder()
                .practitionerId(DUMMY_ID)
                .patientId(DUMMY_ID)
                .startDate(startDate)
                .endDate(startDate.plusHours(1)).build();

        when(practitionerRepository.findById(any()))
                .thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> this.proAppointmentService.save(appointmentDto));
    }

    @Test
    @DisplayName("Checking if there is appointment in the same time or overlapping with it")
    void checkIfAppointmentTimeNotAvailable(){
        LocalDateTime startDate = LocalDateTime.of(2020, Month.FEBRUARY, 5, 11, 0, 0);
        List<Appointment> appointmentList = new ArrayList<>();
        appointmentList.add(new Appointment());

        when(practitionerRepository.findById(any()))
                .thenReturn(Optional.of(new Practitioner()));

        when(patientRepository.findById(any()))
                .thenReturn(Optional.of(new Patient()));

        when(timeSlotRepository.findSurroundingInterval(any(), any(), any()))
                .thenReturn(new ArrayList<>());

        when(appointmentRepository.findAppointmentsInInterval(any(), any(), any()))
                .thenReturn(appointmentList);

        AppointmentDto appointmentDto = AppointmentDto.builder()
                .practitionerId(DUMMY_ID)
                .patientId(DUMMY_ID)
                .startDate(startDate)
                .endDate(startDate.plusHours(1)).build();

        assertThrows(ApiRequestException.class, () -> this.proAppointmentService.save(appointmentDto));
    }

    @Test
    @DisplayName("Checking if there is no available slot in this time of appointment")
    void checkIfNoSlotAvailable(){
        LocalDateTime startDate = LocalDateTime.of(2020, Month.FEBRUARY, 5, 11, 0, 0);
        when(practitionerRepository.findById(any()))
                .thenReturn(Optional.of(new Practitioner()));

        when(patientRepository.findById(any()))
                .thenReturn(Optional.of(new Patient()));

        when(timeSlotRepository.findSurroundingInterval(any(), any(), any()))
                .thenReturn(null);

        AppointmentDto appointmentDto = AppointmentDto.builder()
                .practitionerId(DUMMY_ID)
                .patientId(DUMMY_ID)
                .startDate(startDate)
                .endDate(startDate.plusHours(1)).build();

        assertThrows(ApiRequestException.class, () -> this.proAppointmentService.save(appointmentDto));
    }

    @Test
    @DisplayName("Checking if the appointment time is available")
    void checkIfAppointmentTimeIsAvailable(){
        LocalDateTime startDate = LocalDateTime.of(2020, Month.FEBRUARY, 5, 11, 0, 0);

        when(appointmentRepository.save(any()))
                .thenReturn(Appointment.builder().id(DUMMY_ID).build());
        when(appointmentRepository.findAppointmentsInInterval(any(), any(), any()))
                .thenReturn(new ArrayList<>());

        when(practitionerRepository.findById(any()))
                .thenReturn(Optional.of(new Practitioner()));

        when(patientRepository.findById(any()))
                .thenReturn(Optional.of(new Patient()));

        when(timeSlotRepository.findSurroundingInterval(any(), any(), any()))
                .thenReturn(new ArrayList<>());

        when(appointmentRepository.findAppointmentsInInterval(any(), any(), any()))
                .thenReturn(new ArrayList<>());

        AppointmentDto appointmentDto = AppointmentDto.builder()
                .id(DUMMY_ID)
                .practitionerId(DUMMY_ID)
                .patientId(DUMMY_ID)
                .startDate(startDate)
                .endDate(startDate.plusHours(1)).build();

        assertEquals(this.proAppointmentService.save(appointmentDto), appointmentDto);
    }
}
