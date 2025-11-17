package com.example.TailoredCrochet.services;

import com.example.TailoredCrochet.dto.AuthResponse;
import com.example.TailoredCrochet.dto.RegisterRequest;
import com.example.TailoredCrochet.models.User;
import com.example.TailoredCrochet.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.TailoredCrochet.security.JwtUtils;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    public AuthResponse loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtils.generateJwtToken(user.getEmail());
        return new AuthResponse(token, user.getEmail(), user.getUsername());
    }

    public AuthResponse registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        User savedUser = userRepository.save(user);

        String token = jwtUtils.generateJwtToken(savedUser.getEmail());
        return new AuthResponse(token, savedUser.getEmail(), savedUser.getUsername());
    }
}