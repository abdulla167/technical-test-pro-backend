package com.maiia.pro;

import com.github.javafaker.Faker;
import com.maiia.pro.entity.*;
import com.maiia.pro.repository.PatientRepository;
import com.maiia.pro.repository.PractitionerRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

public class
EntityFactory {
    final Faker faker = new Faker(Locale.FRANCE);

    @Autowired
    public EntityFactory() {}

    public Practitioner createPractitioner() {
        return Practitioner.builder()
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .build();
    }

    public Patient createPatient() {
        return Patient.builder()
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .build();
    }

    public TimeSlot createTimeSlot(Practitioner practitioner, LocalDateTime startDate, LocalDateTime endDate) {
        return TimeSlot.builder()
                .practitioner(practitioner)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    public Appointment createAppointment(Practitioner practitioner, Patient patient, LocalDateTime start, LocalDateTime end) {
        return Appointment.builder()
                .practitioner(practitioner)
                .patient(patient)
                .startDate(start)
                .endDate(end)
                .build();
    }

    public Availability createAvailability(Practitioner practitioner, LocalDateTime start, LocalDateTime end){
        return Availability.builder()
                .practitioner(practitioner)
                .startDate(start)
                .endDate(end)
                .build();
    }
}
