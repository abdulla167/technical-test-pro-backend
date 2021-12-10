package com.maiia.pro.repository;

import com.maiia.pro.EntityFactory;
import com.maiia.pro.entity.Availability;
import com.maiia.pro.entity.Practitioner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class AvailabilityRepositoryTest {

    private final EntityFactory entityFactory;

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Autowired
    private PractitionerRepository practitionerRepository;

    public AvailabilityRepositoryTest() {
        this.entityFactory = new EntityFactory();
    }

    @Test
    void checkIfAvailabilityOverlapWithOverlap() {
        final LocalDateTime startDate = LocalDateTime.of(2020, Month.FEBRUARY, 5, 11, 0, 0);
        Practitioner practitioner = entityFactory.createPractitioner();
        this.practitionerRepository.save(practitioner);
        Availability availability1 = entityFactory.createAvailability(practitioner, startDate, startDate.plusHours(1));
        this.availabilityRepository.save(availability1);

        boolean result = this.availabilityRepository.checkIfAvailabilityOverlap(practitioner,
                                startDate.minusMinutes(20),
                                startDate.plusMinutes(20));

        assertThat(result).isTrue();
    }

    @Test
    void checkIfAvailabilityOverlapWithOutOverlap() {
        final LocalDateTime startDate = LocalDateTime.of(2020, Month.FEBRUARY, 5, 11, 0, 0);
        Practitioner practitioner = entityFactory.createPractitioner();
        this.practitionerRepository.save(practitioner);
        Availability availability1 = entityFactory.createAvailability(practitioner, startDate, startDate.plusHours(1));
        this.availabilityRepository.save(availability1);

        boolean result = this.availabilityRepository.checkIfAvailabilityOverlap(practitioner,
                startDate.minusHours(2),
                startDate.minusMinutes(5));

        assertThat(result).isFalse();
    }
}
