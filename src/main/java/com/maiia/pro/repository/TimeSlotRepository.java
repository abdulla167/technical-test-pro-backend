package com.maiia.pro.repository;

import com.maiia.pro.entity.Practitioner;
import com.maiia.pro.entity.TimeSlot;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TimeSlotRepository extends CrudRepository<TimeSlot, Integer> {
    List<TimeSlot> findByPractitionerId(Integer practitionerId);

    List<TimeSlot> findByPractitioner(Practitioner practitioner);

    @Query(value = "from TimeSlot t where t.startDate <= :startDate AND t.endDate >= :endDate " +
            "AND t.practitioner = :practitioner")
    List<TimeSlot> findSurroundingInterval(@Param("practitioner") Practitioner practitioner,
                                           @Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);
}
