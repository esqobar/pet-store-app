package com.collins.backend.payloads.requests;

import com.collins.backend.entities.Appointment;
import com.collins.backend.entities.Pet;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BookAppointmentRequest {
    private Appointment appointment;
    private List<Pet> pets;
}
