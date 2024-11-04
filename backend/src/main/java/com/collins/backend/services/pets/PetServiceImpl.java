package com.collins.backend.services.pets;

import com.collins.backend.entities.Pet;

import java.util.List;

public interface PetServiceImpl {
    List<Pet> savePetsForAppointment(List<Pet> pets);
    Pet updatePet(Pet pet, Long petId);
    Pet getPetById(Long petId);
    void deletePet( Long petId);
}
