package com.maiia.pro.repository;

import com.maiia.pro.entity.Availability;
import com.maiia.pro.entity.Practitioner;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AvailabilityRepository extends CrudRepository<Availability, Integer> {
    List<Availability> findByPractitionerId(Integer id);

    @Query(value = "SELECT CASE WHEN(count(t) > 0) THEN true ELSE false END " +
            "FROM Availability t WHERE t.practitioner = :practitioner " +
            "AND (t.startDate >= :startDate AND t.startDate < :endDate) OR (t.endDate > :startDate AND t.endDate <= :endDate)")
    boolean checkIfAvailabilityOverlap(@Param("practitioner")Practitioner practitioner,
                                       @Param("startDate") LocalDateTime startDate,
                                       @Param("endDate") LocalDateTime endDate);

    @Modifying
    @Query(value = "DELETE FROM Availability a WHERE a.practitioner = :practitioner AND " +
            "((a.startDate >= :startDate AND a.startDate < :endDate) OR (a.endDate > :startDate AND a.endDate <= :endDate))")
    void deleteAvailabilityInInterval(@Param("practitioner")Practitioner practitioner,
                                      @Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);
}
