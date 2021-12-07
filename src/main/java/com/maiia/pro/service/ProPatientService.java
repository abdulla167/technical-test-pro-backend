package com.maiia.pro.service;

import com.maiia.pro.entity.Patient;
import com.maiia.pro.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProPatientService implements PatientService {

    private final PatientRepository patientRepository;

    @Autowired
    ProPatientService(PatientRepository patientRepository1) {
        this.patientRepository = patientRepository1;
    }

    public Patient find(String patientId) {
        Integer patientIntId = Integer.valueOf(patientId);
        return patientRepository.findById(patientIntId).orElseThrow();
    }

    public List<Patient> findAll() {
        return patientRepository.findAll();
    }
}
