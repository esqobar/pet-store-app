package com.collins.backend.controllers;

import com.collins.backend.entities.Appointment;
import com.collins.backend.exceptions.ResourceNotFoundException;
import com.collins.backend.payloads.requests.AppointmentUpdateRequest;
import com.collins.backend.payloads.responses.ApiResponse;
import com.collins.backend.services.appointments.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.collins.backend.utils.FeedBackMessages.*;
import static com.collins.backend.utils.UrlMappings.*;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(APPOINTMENTS_API)
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping(ALL_APPOINTMENTS)
    public ResponseEntity<ApiResponse> getAllAppointments() {
        try {
            List<Appointment> appointments = appointmentService.getAllAppointments();
            return ResponseEntity.status(OK).body(new ApiResponse(FOUND_SUCCESS, appointments));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping(NEW_APPOINTMENT)
    public ResponseEntity<ApiResponse> bookAppointment(@RequestBody Appointment appointment,
                                                        @RequestParam Long senderId,
                                                        @RequestParam Long recipientId) {
        try {
            Appointment newAppointment = appointmentService.createAppointment(appointment, senderId, recipientId);
            return ResponseEntity.ok(new ApiResponse(APPOINTMENT_SUCCESS, newAppointment));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping(APPOINTMENT_BY_ID)
    public ResponseEntity<ApiResponse> getAppointmentById(@PathVariable Long id) {
        try {
            Appointment appointment = appointmentService.getAppointmentById(id);
            return ResponseEntity.status(OK).body(new ApiResponse(FOUND_SUCCESS, appointment));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping(APPOINTMENT_BY_NO)
    public ResponseEntity<ApiResponse> getAppointmentByNo(@PathVariable String appointmentNo) {
        try {
            Appointment appointment = appointmentService.getAppointmentByNo(appointmentNo);
            return ResponseEntity.status(OK).body(new ApiResponse(FOUND_SUCCESS, appointment));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping(APPOINTMENT_DELETE)
    public ResponseEntity<ApiResponse> deleteAppointmentById(@PathVariable Long id) {
        try {
            appointmentService.deleteAppointment(id);
            return ResponseEntity.ok(new ApiResponse(DELETE_SUCCESS, null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping(APPOINTMENT_UPDATE)
    public ResponseEntity<ApiResponse> updateAppointmentById(@PathVariable Long id, @RequestBody AppointmentUpdateRequest request) {
        try {
            Appointment updateAppointment = appointmentService.updateAppointment(id, request);
            return ResponseEntity.status(OK).body(new ApiResponse(UPDATE_SUCCESS, updateAppointment));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_ACCEPTABLE).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
