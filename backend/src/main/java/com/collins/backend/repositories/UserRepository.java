package com.collins.backend.repositories;

import com.collins.backend.entities.User;
import com.collins.backend.entities.Veterinarian;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    List<Veterinarian> findAllByUserType(String vet);
}
