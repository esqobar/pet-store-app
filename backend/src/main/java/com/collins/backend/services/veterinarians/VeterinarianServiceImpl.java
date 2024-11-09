package com.collins.backend.services.veterinarians;

import com.collins.backend.dtos.UserDto;
import com.collins.backend.entities.Veterinarian;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public interface VeterinarianServiceImpl {
    List<UserDto> getAllVeterinariansWithDetails();

    List<String> getSpecializations();

    List<UserDto> findAvailableVetsForAppointment(String specialization, LocalDate date, LocalTime time);

    List<Veterinarian> getVeterinariansBySpecialization(String specialization);

    List<Map<String, Object>> aggregateVetsBySpecialization();
}
