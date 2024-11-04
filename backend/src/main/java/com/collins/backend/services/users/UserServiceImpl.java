package com.collins.backend.services.users;

import com.collins.backend.dtos.UserDto;
import com.collins.backend.entities.User;
import com.collins.backend.payloads.requests.RegistrationRequest;
import com.collins.backend.payloads.requests.UserUpdateRequest;

import java.util.List;

public interface UserServiceImpl {
    User register(RegistrationRequest request);
    User update(Long userId, UserUpdateRequest request);
    User findById(Long userId);

    void delete(Long userId);

    List<UserDto> getAllUsers();
}
