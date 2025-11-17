package com.example.TailoredCrochet.repositories;

import com.example.TailoredCrochet.models.MeasurementProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MeasurementProfileRepository extends JpaRepository<MeasurementProfile, Long> {
    List<MeasurementProfile> findByUserId(Long userId);
    Optional<MeasurementProfile> findByUserIdAndProfileName (Long userId, String profileName);
}
