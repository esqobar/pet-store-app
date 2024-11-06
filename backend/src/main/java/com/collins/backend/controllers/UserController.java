package com.collins.backend.controllers;

import com.collins.backend.dtos.EntityConverter;
import com.collins.backend.dtos.UserDto;
import com.collins.backend.entities.User;
import com.collins.backend.exceptions.ResourceNotFoundException;
import com.collins.backend.exceptions.UserAlreadyExistsException;
import com.collins.backend.payloads.requests.RegistrationRequest;
import com.collins.backend.payloads.requests.UserUpdateRequest;
import com.collins.backend.payloads.responses.ApiResponse;
import com.collins.backend.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.collins.backend.utils.FeedBackMessages.*;
import static com.collins.backend.utils.UrlMappings.*;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(USERS_API)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final EntityConverter<User, UserDto> entityConverter;

    @PostMapping(NEW_USER)
    public ResponseEntity<ApiResponse> register(@RequestBody RegistrationRequest request) {
        try {
            User newUser = userService.register(request);
            UserDto registerNewUser = entityConverter.mapEntityToDto(newUser, UserDto.class);
            return ResponseEntity.ok(new ApiResponse(CREATE_SUCCESS,registerNewUser));
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping(USER_UPDATE)
    public ResponseEntity<ApiResponse> update(@PathVariable Long userId, @RequestBody UserUpdateRequest request) {
        try {
            User theUser = userService.update(userId, request);
            UserDto updatedUser = entityConverter.mapEntityToDto(theUser, UserDto.class);
            return ResponseEntity.status(OK).body(new ApiResponse(UPDATE_SUCCESS, updatedUser));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

//    @GetMapping(USER_BY_ID)
//    public ResponseEntity<ApiResponse> findById(@PathVariable Long userId) {
//        try {
//            User user = userService.findById(userId);
//            UserDto theUser = entityConverter.mapEntityToDto(user, UserDto.class);
//            return ResponseEntity.status(OK).body(new ApiResponse(FOUND_SUCCESS, theUser));
//        } catch (ResourceNotFoundException e) {
//            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
//        } catch (Exception e) {
//            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
//        }
//    }

    @GetMapping(USER_BY_ID)
    public ResponseEntity<ApiResponse> findById(@PathVariable Long userId) {
        try {
            UserDto userDto = userService.getUserWithDetails(userId);
            return ResponseEntity.status(FOUND).body(new ApiResponse(FOUND_SUCCESS, userDto));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping(USER_DELETE)
    public ResponseEntity<ApiResponse> deleteById(@PathVariable Long userId) {
        try {
            userService.delete(userId);
            return ResponseEntity.ok(new ApiResponse(DELETE_SUCCESS, null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping(ALL_USERS)
    public ResponseEntity<ApiResponse> getAllUsers() {
        try {
            List<UserDto> allUsers = userService.getAllUsers();
            return ResponseEntity.status(OK).body(new ApiResponse(FOUND_SUCCESS, allUsers));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
