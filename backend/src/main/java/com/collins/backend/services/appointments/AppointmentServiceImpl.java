package com.collins.backend.services.appointments;

import com.collins.backend.entities.Appointment;
import com.collins.backend.payloads.requests.AppointmentUpdateRequest;

import java.util.List;

public interface AppointmentServiceImpl {
    Appointment createAppointment(Appointment appointment, Long sender, Long recipient);
    List<Appointment> getAllAppointments();
    Appointment updateAppointment(Long id, AppointmentUpdateRequest request);
    void deleteAppointment(Long id);
    Appointment getAppointmentById(Long id);
    Appointment getAppointmentByNo(String appointmentNo);
}
