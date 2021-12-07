package com.maiia.pro.template;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AppointmentResponseMessage {
    NOT_AVAILABLE_APPOINTMENT_TIME("This time is not available"),
    APPOINTMENT_CREATED("Appointment has been created");
    private final String responseMessage;
}

