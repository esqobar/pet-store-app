package com.collins.backend.controllers;

import com.collins.backend.dtos.UserDto;
import com.collins.backend.payloads.responses.ApiResponse;
import com.collins.backend.services.veterinarians.VeterinarianService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.collins.backend.utils.FeedBackMessages.*;
import static com.collins.backend.utils.UrlMappings.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping(VETERINARIANS_API)
@RequiredArgsConstructor
public class VeterinarianController {
    private final VeterinarianService veterinarianService;

    @GetMapping(ALL_VETERINARIANS)
    public ResponseEntity<ApiResponse> getAllVeterinarians() {
        List<UserDto> allVeterinarians = veterinarianService.getAllVeterinariansWithDetails();
        return ResponseEntity.status(OK).body(new ApiResponse(FOUND_SUCCESS, allVeterinarians));
    }

    @GetMapping(SEARCH_VETERINARIANS_FOR_APPOINTMENT)
    public ResponseEntity<ApiResponse> searchVeterinariansForAppointment(
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) LocalTime time,
            @RequestParam String specialization) {
        try {
            List<UserDto> availableVets = veterinarianService.findAvailableVetsForAppointment(specialization, date, time);
            if (availableVets.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(NO_VETS_FOUND_FOR_APPOINTMENT, null));
            }
            return ResponseEntity.status(OK).body(new ApiResponse(FOUND_SUCCESS, availableVets));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

}
