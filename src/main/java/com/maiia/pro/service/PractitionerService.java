package com.maiia.pro.service;

import com.maiia.pro.entity.Practitioner;

import java.util.List;

public interface PractitionerService {

    public Practitioner find(String practitionerId);

    public List<Practitioner> findAll();
}
