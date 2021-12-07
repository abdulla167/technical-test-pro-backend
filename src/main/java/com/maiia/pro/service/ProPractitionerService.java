package com.maiia.pro.service;

import com.maiia.pro.entity.Practitioner;
import com.maiia.pro.repository.PractitionerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProPractitionerService implements PractitionerService {

    private final PractitionerRepository practitionerRepository;

    @Autowired
    ProPractitionerService(PractitionerRepository practitionerRepository) {
        this.practitionerRepository = practitionerRepository;
    }

    public Practitioner find(String practitionerId) {
        Integer practitionerIntId = Integer.valueOf(practitionerId);
        return practitionerRepository.findById(practitionerIntId).orElseThrow();
    }

    public List<Practitioner> findAll() {
        return practitionerRepository.findAll();
    }
}
