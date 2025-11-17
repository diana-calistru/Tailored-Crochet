package com.example.TailoredCrochet.repositories;

import com.example.TailoredCrochet.models.Part;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartRepository extends JpaRepository<Part, Long> {
    Optional<Part> findByPartTypeAndPartStyle (String partType, String partStyle);
}