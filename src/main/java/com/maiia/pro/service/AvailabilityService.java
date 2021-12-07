package com.maiia.pro.service;

import com.maiia.pro.dto.AvailabilityDto;
import com.maiia.pro.entity.Availability;

import java.util.List;

public interface AvailabilityService {

    public List<Availability> generateAvailabilities(Integer practitionerId);

    public List<AvailabilityDto> findByPractitionerId(Integer practitionerId);
}
