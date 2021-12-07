package com.maiia.pro.service;

import com.maiia.pro.entity.TimeSlot;
import com.maiia.pro.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProTimeSlotService implements TimeSlotService {

    private final TimeSlotRepository timeSlotRepository;

    @Autowired
    ProTimeSlotService(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }

    public List<TimeSlot> findByPractitionerId(String practitionerId) {
        Integer practitionerIdInt = Integer.valueOf(practitionerId);
        return timeSlotRepository.findByPractitionerId(practitionerIdInt);
    }
}
