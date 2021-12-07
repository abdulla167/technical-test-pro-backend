package com.maiia.pro.service;

import com.maiia.pro.dto.AppointmentDto;
import com.maiia.pro.entity.Appointment;

import java.util.List;

public interface AppointmentService {

    public AppointmentDto save(final AppointmentDto appointmentDto);

    public List<Appointment> findAll();

    public Appointment find(final String appointmentId);

    public List<Appointment> findByPractitionerId(final String practitionerId);

}
