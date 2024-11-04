package com.collins.backend.services.users;

import com.collins.backend.dtos.EntityConverter;
import com.collins.backend.dtos.UserDto;
import com.collins.backend.entities.User;
import com.collins.backend.exceptions.ResourceNotFoundException;
import com.collins.backend.factories.UserFactory;
import com.collins.backend.payloads.requests.RegistrationRequest;
import com.collins.backend.payloads.requests.UserUpdateRequest;
import com.collins.backend.repositories.UserRepository;
import com.collins.backend.utils.FeedBackMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.collins.backend.utils.FeedBackMessages.*;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceImpl {
    private final UserRepository userRepository;
    private final UserFactory userFactory;
    private final EntityConverter<User, UserDto> entityConverter;

    @Override
    public User register(RegistrationRequest request) {
        return userFactory.createUser(request);
    }

    @Override
    public User update(Long userId, UserUpdateRequest request) {
        User user = findById(userId);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setGender(request.getGender());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setSpecialization(request.getSpecialization());
        return userRepository.save(user);
    }

    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOT_FOUND));
    }

    @Override
    public void delete(Long userId) {
        userRepository.findById(userId)
                .ifPresentOrElse( userRepository::delete, () -> {
                    throw new ResourceNotFoundException(RESOURCE_NOT_FOUND);
                });
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> entityConverter.mapEntityToDto(user, UserDto.class))
                .collect(Collectors.toList());
    }

}
