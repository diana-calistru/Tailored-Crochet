package com.example.TailoredCrochet.controllers;


import com.example.TailoredCrochet.dto.MeasurementResultDTO;
import com.example.TailoredCrochet.services.MeasurementProfileService;
import com.example.TailoredCrochet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/profiles")
public class MeasurementProfileController {

    @Autowired
    private MeasurementProfileService measurementProfileService;

    @PostMapping("/create")
    public ResponseEntity<MeasurementResultDTO> createProfile(
            @RequestParam("email") String email,
            @RequestParam("name") String name,
            @RequestParam("notes") String notes,
            @RequestParam("height") double height,
            @RequestParam("image") MultipartFile image )
    {
        try {
            Map<String, Double> measurements = measurementProfileService.createProfile(email, name, notes, height, image);
            return ResponseEntity.ok(new MeasurementResultDTO(name, notes, measurements));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @GetMapping("/get")
    public ResponseEntity<List<MeasurementResultDTO>> getProfiles(
            @RequestParam("email") String email )
    {
        try {
            List<MeasurementResultDTO> measurementResultDTOS = measurementProfileService.getProfiles(email);
            return ResponseEntity.ok(measurementResultDTOS);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteProfile(
            @RequestParam("email") String email,
            @RequestParam("name") String name)
    {
        try {
            measurementProfileService.deleteProfile(email, name);
            return ResponseEntity.ok("Profile deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
