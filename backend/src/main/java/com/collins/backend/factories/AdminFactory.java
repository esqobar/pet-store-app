package com.collins.backend.factories;

import com.collins.backend.entities.Admin;
import com.collins.backend.entities.User;
import com.collins.backend.payloads.requests.RegistrationRequest;
import com.collins.backend.repositories.AdminRepository;
import com.collins.backend.services.users.UserAttributesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminFactory {
    private final UserAttributesMapper userAttributesMapper;
    private final AdminRepository adminRepository;

    public User createAdmin(RegistrationRequest request) {
        Admin admin = new Admin();
        userAttributesMapper.setCommonAttributes(request, admin);
        return adminRepository.save(admin);
    }
}
