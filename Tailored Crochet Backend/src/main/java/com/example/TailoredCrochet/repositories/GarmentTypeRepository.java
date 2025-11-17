package com.example.TailoredCrochet.repositories;

import com.example.TailoredCrochet.models.GarmentType;
import com.example.TailoredCrochet.models.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GarmentTypeRepository extends JpaRepository<GarmentType, Long> {


    Optional<GarmentType> findByGarmentName(String garmentName);

    @Query("SELECT DISTINCT gt FROM GarmentType gt LEFT JOIN FETCH gt.parts WHERE gt.garmentName = :garmentName")
    Optional<GarmentType> findByGarmentNameWithParts(@Param("garmentName") String garmentName);
}
