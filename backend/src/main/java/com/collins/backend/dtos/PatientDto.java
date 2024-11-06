package com.collins.backend.dtos;

import lombok.Data;

@Data
public class PatientDto {
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private String phoneNumber;
}
