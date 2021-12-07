package com.maiia.pro.dto;

import com.maiia.pro.entity.Appointment;
import com.maiia.pro.entity.Availability;
import org.springframework.stereotype.Component;

@Component
public class EntityDtoMapper {

    /**
     * This method is used to map Appointment entity to Appointment data
     * transfer object to be sent to the client
     *
     * @param appointment : The appointment that need to be converted
     * @return            : Data transfer object of type AppointmentDto
     */
    public AppointmentDto appointmentToAppointmentDto(Appointment appointment){
        return AppointmentDto.builder()
                .id(appointment.getId())
                .practitionerId(appointment.getPractitioner().getId())
                .patientId(appointment.getPatient().getId())
                .startDate(appointment.getStartDate())
                .endDate(appointment.getEndDate()).build();
    }

    /**
     * This method is used to map Availability entity to Availability data
     * transfer object to be sent to the client
     *
     * @param availability : The availability that need to be converted
     * @return             : Data transfer object of type AvailabilityDto
     */
    public AvailabilityDto availabilityToAvailabilityDto(Availability availability){
        return AvailabilityDto.builder()
                .id(availability.getId())
                .practitionerId(availability.getPractitioner().getId())
                .startDate(availability.getStartDate())
                .endDate(availability.getEndDate())
                .build();
    }
}
