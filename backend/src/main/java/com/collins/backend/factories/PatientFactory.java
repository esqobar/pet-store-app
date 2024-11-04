package com.collins.backend.factories;

import com.collins.backend.entities.Patient;
import com.collins.backend.entities.User;
import com.collins.backend.payloads.requests.RegistrationRequest;
import com.collins.backend.repositories.PatientRepository;
import com.collins.backend.services.users.UserAttributesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientFactory {
    private final UserAttributesMapper userAttributesMapper;
    private final PatientRepository patientRepository;

    public User createPatient(RegistrationRequest request) {
        Patient patient = new Patient();
        userAttributesMapper.setCommonAttributes(request, patient);
        return patientRepository.save(patient);
    }
}
