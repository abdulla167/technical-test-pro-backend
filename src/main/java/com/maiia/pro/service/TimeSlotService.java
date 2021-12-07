package com.maiia.pro.service;

import com.maiia.pro.entity.TimeSlot;

import java.util.List;

public interface TimeSlotService {
    public List<TimeSlot> findByPractitionerId(String practitionerId);
}
