package com.collins.backend.factories;

import com.collins.backend.entities.User;
import com.collins.backend.exceptions.UserAlreadyExistsException;
import com.collins.backend.payloads.requests.RegistrationRequest;
import com.collins.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SimpleUserFactory implements UserFactory {

    private final UserRepository userRepository;
    private final VeterinarianFactory veterinarianFactory;
    private final PatientFactory patientFactory;
    private final AdminFactory adminFactory;

    @Override
    public User createUser(RegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Oops! " + request.getEmail() + " already exists!");
        }
        switch (request.getUserType()) {
            case "VET" -> {
                return veterinarianFactory.createVeterinarian(request);
            }
            case "PATIENT" -> {
                return patientFactory.createPatient(request);
            }
            case "ADMIN" -> {
                return adminFactory.createAdmin(request);
            }
            default -> {
                return null;
            }
        }

    }
}