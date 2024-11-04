package com.collins.backend.factories;

import com.collins.backend.entities.User;
import com.collins.backend.payloads.requests.RegistrationRequest;

public interface UserFactory {
    public User createUser(RegistrationRequest request);
}
