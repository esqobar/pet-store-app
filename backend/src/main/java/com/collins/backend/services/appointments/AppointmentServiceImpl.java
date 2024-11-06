package com.collins.backend.services.appointments;

import com.collins.backend.dtos.AppointmentDto;
import com.collins.backend.entities.Appointment;
import com.collins.backend.payloads.requests.AppointmentUpdateRequest;
import com.collins.backend.payloads.requests.BookAppointmentRequest;

import java.util.List;

public interface AppointmentServiceImpl {
    Appointment createAppointment(BookAppointmentRequest request, Long sender, Long recipient);
    List<Appointment> getAllAppointments();
    Appointment updateAppointment(Long id, AppointmentUpdateRequest request);
    void deleteAppointment(Long id);
    Appointment getAppointmentById(Long id);
    Appointment getAppointmentByNo(String appointmentNo);

    List<AppointmentDto> getUserAppointments(Long userId);
}
