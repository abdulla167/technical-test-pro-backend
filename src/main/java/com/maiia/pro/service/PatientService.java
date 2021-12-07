package com.maiia.pro.service;

import com.maiia.pro.entity.Patient;

import java.util.List;

public interface PatientService {

    public Patient find(String patientId);

    public List<Patient> findAll();
}
