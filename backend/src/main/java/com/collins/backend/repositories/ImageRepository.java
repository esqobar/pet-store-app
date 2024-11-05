package com.collins.backend.repositories;

import com.collins.backend.entities.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Photo, Long> {
}
