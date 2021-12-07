package com.maiia.pro.repository;

import com.maiia.pro.entity.Appointment;
import com.maiia.pro.entity.Practitioner;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends CrudRepository<Appointment, Integer> {
    List<Appointment> findAll();

    List<Appointment> findByPractitioner(Practitioner practitioner);

    List<Appointment> findByPractitionerOrderByStartDate(Practitioner practitioner);

    @Query(value = "from Appointment t where t.practitioner = :practitioner " +
            "AND (t.startDate > :startDate AND t.startDate < :endDate) OR (t.endDate > :startDate AND t.endDate < :endDate)")
    List<Appointment> findAppointmentsInInterval(@Param("practitioner") Practitioner practitioner
            , @Param("startDate") LocalDateTime startDate, @Param("endDate")LocalDateTime endDate);
}
