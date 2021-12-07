package com.maiia.pro.repository;

import com.maiia.pro.EntityFactory;
import com.maiia.pro.entity.Appointment;
import com.maiia.pro.entity.Patient;
import com.maiia.pro.entity.Practitioner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class AppointmentRepositoryTest {

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    PractitionerRepository practitionerRepository;

    @Autowired
    PatientRepository patientRepository;

    EntityFactory entityFactory;

    AppointmentRepositoryTest() {
        entityFactory = new EntityFactory();
    }

    @Test
    void injectedComponentsAreNotNull(){
        assertThat(practitionerRepository).isNotNull();
        assertThat(appointmentRepository).isNotNull();
        assertThat(patientRepository).isNotNull();
    }

    @Test
    void findAppointmentInIntervalTestWithTwoAppointments(){
        final LocalDateTime startDate = LocalDateTime.of(2020, Month.FEBRUARY, 5, 11, 0, 0);
        final Practitioner practitioner = entityFactory.createPractitioner();
        final Patient patient = entityFactory.createPatient();

        final Appointment appointment1 = entityFactory.createAppointment(practitioner, patient,
                    startDate, startDate.plusHours(1));
        final Appointment appointment2 =  entityFactory.createAppointment(practitioner, patient,
                    startDate.plusMinutes(80), startDate.plusMinutes(120));

        this.practitionerRepository.save(practitioner);
        this.patientRepository.save(patient);
        this.appointmentRepository.save(appointment1);
        this.appointmentRepository.save(appointment2);
        final Appointment appointment = this.appointmentRepository.findAppointmentsInInterval(practitioner,
                    startDate.plusMinutes(70),
                    startDate.plusHours(90)).get(0);
        assertThat(appointment.getStartDate()).isEqualTo(appointment2.getStartDate());
        assertThat(appointment.getEndDate()).isEqualTo(appointment2.getEndDate());
        assertThat(appointment.getPractitioner().getId()).isEqualTo(appointment2.getPractitioner().getId());
    }

    @Test
    void findAppointmentInIntervalTestWithNoAppointments(){
        final LocalDateTime startDate = LocalDateTime.of(2020, Month.FEBRUARY, 5, 11, 0, 0);
        final Practitioner practitioner = entityFactory.createPractitioner();
        this.practitionerRepository.save(practitioner);
        final List<Appointment> appointments = this.appointmentRepository.findAppointmentsInInterval(practitioner,
                    startDate,
                    startDate.plusHours(90));
        assertThat(appointments).isEmpty();
    }
}
