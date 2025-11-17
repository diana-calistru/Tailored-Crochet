package com.example.TailoredCrochet.services;

import com.example.TailoredCrochet.dto.MeasurementResultDTO;
import com.example.TailoredCrochet.models.MeasurementProfile;
import com.example.TailoredCrochet.models.User;
import com.example.TailoredCrochet.repositories.MeasurementProfileRepository;
import com.example.TailoredCrochet.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MeasurementProfileService {

    @Autowired
    private MeasurementProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    private final Path uploadDir = Paths.get("uploads");

    public Map<String, Double> runInference(MultipartFile imageFile, double heightCm) throws IOException, InterruptedException {
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        String filename = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
        Path filePath = uploadDir.resolve(filename);
        Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);


        ProcessBuilder pb = new ProcessBuilder(
                "python",
                "D:\\School Materials\\Final Paper\\App\\BodyMeasurementExtraction\\main.py",
                filePath.toAbsolutePath().toString(),
                String.valueOf(heightCm)
        );
        pb.redirectErrorStream(true);
        Process process = pb.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        String lastJsonLine = null;

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (!line.isEmpty()) {
                lastJsonLine = line;
            }
        }

        if (lastJsonLine == null) {
            throw new RuntimeException("No output from Python script.");
        }

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(lastJsonLine, Map.class);
    }

    public Map<String, Double> createProfile(String email, String name, String notes, double height, MultipartFile image)
            throws IOException, InterruptedException {

        Map<String, Double> measurements = runInference(image, height);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        MeasurementProfile profile = new MeasurementProfile();
        profile.setProfileName(name);
        profile.setNotes(notes);
        profile.setHeight(height);
        profile.setUser(user);

        profile.setChestCirc(measurements.getOrDefault("chest circ", 0.0));
        profile.setWaistCirc(measurements.getOrDefault("waist circ", 0.0));
        profile.setPelvisCirc(measurements.getOrDefault("pelvis circ", 0.0));
        profile.setNeckCirc(measurements.getOrDefault("neck circ", 0.0));
        profile.setBicepCirc(measurements.getOrDefault("bicep circ", 0.0));
        profile.setThighCirc(measurements.getOrDefault("thigh circ", 0.0));
        profile.setKneeCirc(measurements.getOrDefault("knee circ", 0.0));
        profile.setArmLength(measurements.getOrDefault("arm length", 0.0));
        profile.setLegLength(measurements.getOrDefault("leg length", 0.0));
        profile.setCalfLength(measurements.getOrDefault("calf length", 0.0));
        profile.setHeadCirc(measurements.getOrDefault("head circ", 0.0));
        profile.setWristCirc(measurements.getOrDefault("wrist circ", 0.0));
        profile.setArmSpan(measurements.getOrDefault("arm span", 0.0));
        profile.setShouldersWidth(measurements.getOrDefault("shoulders width", 0.0));
        profile.setTorsoLength(measurements.getOrDefault("torso length", 0.0));
        profile.setInnerLeg(measurements.getOrDefault("inner leg", 0.0));

        profileRepository.save(profile);
        return measurements;
    }

    public List<MeasurementResultDTO> getProfiles(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<MeasurementProfile> profiles = profileRepository.findByUserId(user.getId());

        return profiles.stream()
                .map(MeasurementResultDTO::new)
                .collect(Collectors.toList());
    }

    public void deleteProfile(String email, String name) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        MeasurementProfile profile = profileRepository.findByUserIdAndProfileName(user.getId(), name)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        profileRepository.delete(profile);
    }
}