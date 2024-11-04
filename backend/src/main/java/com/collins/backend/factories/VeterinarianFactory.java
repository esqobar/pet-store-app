package com.collins.backend.factories;

import com.collins.backend.entities.User;
import com.collins.backend.entities.Veterinarian;
import com.collins.backend.payloads.requests.RegistrationRequest;
import com.collins.backend.repositories.VeterinarianRepository;
import com.collins.backend.services.users.UserAttributesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VeterinarianFactory {
    private final VeterinarianRepository veterinarianRepository;
    private final UserAttributesMapper userAttributesMapper;

    public User createVeterinarian(RegistrationRequest request) {
        Veterinarian veterinarian = new Veterinarian();
        userAttributesMapper.setCommonAttributes(request, veterinarian);
        veterinarian.setSpecialization(request.getSpecialization());
        return veterinarianRepository.save(veterinarian);
    }
}
