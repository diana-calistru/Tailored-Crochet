package com.example.TailoredCrochet.repositories;

import com.example.TailoredCrochet.models.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PatternRepository extends JpaRepository<Pattern, Long> {
    List<Pattern> findByUserId(Long userId);

    @Query("SELECT DISTINCT p FROM Pattern p " +
            "LEFT JOIN FETCH p.parts " +
            "WHERE p.user.id = :userId")
    List<Pattern> findByUserIdWithParts(@Param("userId") Long userId);
}