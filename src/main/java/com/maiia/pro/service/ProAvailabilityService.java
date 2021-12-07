package com.maiia.pro.service;

import com.maiia.pro.dto.AvailabilityDto;
import com.maiia.pro.dto.EntityDtoMapper;
import com.maiia.pro.entity.Appointment;
import com.maiia.pro.entity.Availability;
import com.maiia.pro.entity.Practitioner;
import com.maiia.pro.entity.TimeSlot;
import com.maiia.pro.exception.ApiRequestException;
import com.maiia.pro.repository.AppointmentRepository;
import com.maiia.pro.repository.AvailabilityRepository;
import com.maiia.pro.repository.PractitionerRepository;
import com.maiia.pro.repository.TimeSlotRepository;
import com.maiia.pro.utilities.EmptyInterval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProAvailabilityService implements AvailabilityService {

    private final AvailabilityRepository availabilityRepository;

    private final AppointmentRepository appointmentRepository;

    private final TimeSlotRepository timeSlotRepository;

    private final PractitionerRepository practitionerRepository;

    private final EntityDtoMapper entityDtoMapper;

    @Autowired
    ProAvailabilityService(AvailabilityRepository availabilityRepository1,
                           AppointmentRepository appointmentRepository1,
                           TimeSlotRepository timeSlotRepository1,
                           PractitionerRepository practitionerRepository1,
                           EntityDtoMapper entityDtoMapper){
        this.availabilityRepository = availabilityRepository1;
        this.appointmentRepository = appointmentRepository1;
        this.timeSlotRepository = timeSlotRepository1;
        this.practitionerRepository = practitionerRepository1;
        this.entityDtoMapper = entityDtoMapper;
    }

    public List<AvailabilityDto> findByPractitionerId(Integer practitionerId) {
        return availabilityRepository.findByPractitionerId(practitionerId).stream()
                .map(entityDtoMapper::availabilityToAvailabilityDto)
                .collect(Collectors.toList());
    }

    /**
     * This method is used to generate all the possible availabilities of a specific practitioner.
     * @param practitionerId : The id of the practitioner that we need to generate his availabilities
     * @return               : List with all the possible availabilities in the practitioner slots
     */
    public List<Availability> generateAvailabilities(Integer practitionerId) {

        List<Availability> availabilityList = new ArrayList<>();
        List<EmptyInterval> emptyIntervals = new ArrayList<>();

        // Check if practitioner exist
        Optional<Practitioner> practitioner = this.practitionerRepository.findById(practitionerId);
        practitioner.orElseThrow(() -> new ApiRequestException("This practitioner is not exist"));

        // Get practitioner's timeslots and appointments
        List<TimeSlot> timeSlots = this.timeSlotRepository.findByPractitioner(practitioner.get());
        List<Appointment> appointmentList = this.appointmentRepository.findByPractitionerOrderByStartDate(practitioner.get());

        timeSlots.forEach(timeSlot -> {
            // Define slot start and end points
            final LocalDateTime slotStartTime = timeSlot.getStartDate();
            final LocalDateTime slotEndTime = timeSlot.getEndDate();

            // If there is no appointments, loop on every 15 MIN in the slot and create availability
            if (appointmentList.isEmpty()){
                this.addPossibleAvailabilities(slotStartTime, slotEndTime, availabilityList, practitioner.get(), true);
            } else {
                // filter the appointments to get the appointments in the current slot
                List<Appointment> timeIntervalAppointments = appointmentList.stream()
                        .filter(appointment -> (appointment.getStartDate().equals(slotStartTime)
                                ||  appointment.getStartDate().isAfter(slotStartTime))
                                && appointment.getStartDate().isBefore(slotEndTime))
                        .collect(Collectors.toList());

                // Get map of the empty intervals in the slot
                getEmptyIntervals(slotStartTime, slotEndTime, timeIntervalAppointments, emptyIntervals);
            }
        });

        // Loop on each element in map and create related availabilities
        int intervalCounter = 0;
        boolean lastIntervalFlag = false;
        for (EmptyInterval emptyInterval : emptyIntervals) {
            if (intervalCounter == emptyIntervals.size() - 1)
                lastIntervalFlag = true;
            this.addPossibleAvailabilities(emptyInterval.getStartDate(), emptyInterval.getEndDate(),
                    availabilityList, practitioner.get(), lastIntervalFlag);
            intervalCounter++;
        }

        return availabilityList;
    }

    /**
     * This method is used to get the empty intervals in a slot of practitioner by using
     * appointments in this slot.
     * @param slotStart              : Start time of the slot
     * @param slotEnd                : End time of the slot
     * @param appointments           : List of appointments in the slot
     * @param expectedAvailabilities : The list where the empty intervals which will be found will put in
     */
    private void getEmptyIntervals(final LocalDateTime slotStart,
                                   final LocalDateTime slotEnd,
                                   final List<Appointment> appointments,
                                   final List<EmptyInterval> expectedAvailabilities){
        int numOfAppointments = appointments.size();
        for (Appointment appointment : appointments) {
            LocalDateTime availabilitiesStart;
            int indexOfCurrentAppointment = appointments.indexOf(appointment);
            if (indexOfCurrentAppointment == 0 && (!slotStart.equals(appointments.get(0).getStartDate()))){
                LocalDateTime currentAppointmentStartDate = appointments.get(indexOfCurrentAppointment).getStartDate();
                expectedAvailabilities.add(new EmptyInterval(slotStart, currentAppointmentStartDate));
            }
            if (indexOfCurrentAppointment == (numOfAppointments - 1)){
                availabilitiesStart = appointments.get(indexOfCurrentAppointment).getEndDate();
                expectedAvailabilities.add(new EmptyInterval(availabilitiesStart, slotEnd));
            } else {
                availabilitiesStart = appointments.get(indexOfCurrentAppointment).getEndDate();
                LocalDateTime nextAppointmentStartDate = appointments.get(indexOfCurrentAppointment + 1).getStartDate();
                expectedAvailabilities.add(new EmptyInterval(availabilitiesStart, nextAppointmentStartDate));
            }
        }
    }

    /**
     * This method is looping on an empty interval and get out all the possible
     * availabilities found
     * @param intervalStart    : The start time of the empty interval.
     * @param intervalEnd      : The end time of the empty interval.
     * @param availabilityList : The list where the result of the possible availabilities will put in.
     * @param practitioner     : The practitioner of the appointment.
     * @param lastIntervalFlag : This flag indicate if the current interval is the last interval or not.
     */
    private void addPossibleAvailabilities(final LocalDateTime intervalStart,
                                           final LocalDateTime intervalEnd,
                                           final List<Availability> availabilityList,
                                           final Practitioner practitioner,
                                           final boolean lastIntervalFlag) {

        LocalDateTime availabilityStart = intervalStart;
        LocalDateTime availabilityEnd;
        // Array to store need to saved availabilities
        ArrayList<Availability> willBeSavedAvailabilities = new ArrayList<>();

        // Check is there is more time for availability & check for last interval for optimal result
        while (availabilityStart.isBefore(intervalEnd) &&
                (!availabilityStart.plusMinutes(15).isAfter(intervalEnd) || lastIntervalFlag)){
            availabilityEnd = availabilityStart.plusMinutes(15);
            Availability availability = Availability.builder().practitioner(practitioner)
                    .startDate(availabilityStart)
                    .endDate(availabilityEnd).build();
            // Check if there is another availability at this time
            if (!this.availabilityRepository.checkIfAvailabilityOverlap(practitioner,
                    availabilityStart, availabilityEnd)){
                willBeSavedAvailabilities.add(availability);
            }
            availabilityList.add(availability);
            availabilityStart = availabilityEnd;
        }
        this.availabilityRepository.saveAll(willBeSavedAvailabilities);
    }
}
