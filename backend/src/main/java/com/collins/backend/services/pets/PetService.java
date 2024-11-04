package com.collins.backend.services.pets;

import com.collins.backend.entities.Pet;
import com.collins.backend.exceptions.ResourceNotFoundException;
import com.collins.backend.repositories.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.collins.backend.utils.FeedBackMessages.RESOURCE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PetService implements PetServiceImpl{

    private final PetRepository petRepository;

    @Override
    public List<Pet> savePetsForAppointment(List<Pet> pets) {
        return petRepository.saveAll(pets);
    }

    @Override
    public Pet updatePet(Pet pet, Long petId) {
        Pet existingPet = getPetById(petId);
        existingPet.setName(pet.getName());
        existingPet.setAge(pet.getAge());
        existingPet.setColor(pet.getColor());
        existingPet.setType(pet.getType());
        existingPet.setBreed(pet.getBreed());
        return petRepository.save(existingPet);
    }

    @Override
    public Pet getPetById(Long petId) {
        return petRepository.findById(petId)
                .orElseThrow(() ->new ResourceNotFoundException(RESOURCE_NOT_FOUND));
    }

    @Override
    public void deletePet(Long petId) {
        petRepository.findById(petId)
                .ifPresentOrElse(petRepository::delete, () -> {
                    throw new ResourceNotFoundException(RESOURCE_NOT_FOUND);
                });
    }
}
