package com.maiia.pro.service;

import com.maiia.pro.dto.AppointmentDto;
import com.maiia.pro.entity.*;
import com.maiia.pro.exception.ApiRequestException;
import com.maiia.pro.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProAppointmentService implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final PractitionerRepository practitionerRepository;
    private final PatientRepository patientRepository;
    private final AvailabilityRepository availabilityRepository;

    private final AvailabilityService availabilityService;

    @Autowired
    ProAppointmentService(final AppointmentRepository appointmentRepository1,
                          final TimeSlotRepository timeSlotRepository1,
                          final PractitionerRepository practitionerRepository1,
                          final PatientRepository patientRepository1,
                          AvailabilityRepository availabilityRepository,
                          AvailabilityService availabilityService) {
        this.appointmentRepository = appointmentRepository1;
        this.timeSlotRepository = timeSlotRepository1;
        this.practitionerRepository = practitionerRepository1;
        this.patientRepository = patientRepository1;
        this.availabilityRepository = availabilityRepository;
        this.availabilityService = availabilityService;
    }

    /**
     * This method is used to get an appointment
     * by the appointment id.
     *
     * @param appointmentId : The id of requested appointment
     * @return              : List of the saved practitioner id
     */
    public Appointment find(final String appointmentId) {
        Integer appointmentIdInt = Integer.valueOf(appointmentId);
        return appointmentRepository.findById(appointmentIdInt).orElseThrow();
    }

    /**
     * This method is used to get all the appointments
     * of all practitioner from the database.
     *
     * @return               : List of the saved practitioner id
     */
    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    /**
     * This method is used to get the appointments of practitioner from the database
     * by the practitioner id and throw ApiRequestException if the practitioner is
     * not found
     *
     * @param practitionerId : The request practitioner id
     * @return               : List of the saved practitioner id
     */
    public List<Appointment> findByPractitionerId(final String practitionerId) {
        int practitionerIdInt;
        try {
            practitionerIdInt = Integer.parseInt(practitionerId);
        } catch (NumberFormatException e){
            throw new ApiRequestException("Practitioner id should be a number");
        }

        // Check if practitioner is found or not
        Optional<Practitioner> practitioner = this.practitionerRepository
                .findById(practitionerIdInt);
        if (practitioner.isEmpty()) {
            throw new ApiRequestException("There is no practitioner with this id");
        }

        return appointmentRepository.findByPractitioner(practitioner.get());
    }

    /**
     * This method is used to save new appointment
     * between a practitioner and patient.
     *
     * @param appointmentDto: The data transfer object which has information about appointment
     * @return              : The Information of saved entity in th database
     */
    @Transactional
    public AppointmentDto save(final AppointmentDto appointmentDto) {
        LocalDateTime appointmentStart = appointmentDto.getStartDate();
        LocalDateTime appointmentEnd = appointmentDto.getEndDate();

        // Validate if end date after start date
        if (!appointmentEnd.isAfter(appointmentStart)){
            throw new ApiRequestException("The end date should be after the start date");
        }

        // Get patient of the appointment.
        Optional<Practitioner> practitioner = this.practitionerRepository
                .findById(appointmentDto.getPractitionerId());
        practitioner.orElseThrow(() -> new ApiRequestException("Practitioner of the appointment doesn't exist"));

        // Get practitioner of the appointment
        Optional<Patient> patient = this.patientRepository
                .findById(appointmentDto.getPatientId());
        patient.orElseThrow(() -> new ApiRequestException("Patient of the appointment doesn't exist"));

        if (!this.checkAvailabilityOfAppointment(practitioner.get(),
                appointmentStart, appointmentEnd)) {
            throw new ApiRequestException("There is no available time for this appointment");
        }
        Appointment appointment = Appointment.builder()
                .patient(patient.get())
                .practitioner(practitioner.get())
                .startDate(appointmentStart)
                .endDate(appointmentEnd)
                .build();
        Appointment savedAppointment = this.appointmentRepository
                .save(appointment);
        appointmentDto.setId(savedAppointment.getId());
        this.availabilityRepository.deleteAvailabilityInInterval(practitioner.get(), appointmentStart, appointmentEnd);
        this.availabilityService.generateAvailabilities(practitioner.get().getId());
        return appointmentDto;
    }

     /**
     * This method is used to check if there is available time
     * for the appointment to decide to save it or not.
     *
     * @param practitioner: The practitioner of the appointment
     * @param startTime   : The start time for the appointment
     * @param endTime     : The end time of the appointment
     * @return            : A flag to indicate if time is available or not
     */
    private boolean checkAvailabilityOfAppointment(final Practitioner practitioner,
                                                   final LocalDateTime startTime,
                                                   final LocalDateTime endTime) {

        // Check if there is a slot in this time
        List<TimeSlot> timeSlots = this.timeSlotRepository
                .findSurroundingInterval(practitioner, startTime, endTime);
        if (timeSlots == null) {
            return false;
        }

        // Check if there is another appointment in the same time
        List<Appointment> appointmentList = this.appointmentRepository
                .findAppointmentsInInterval(practitioner, startTime, endTime);
        if (!appointmentList.isEmpty())
            return false;
        return true;
    }

}

