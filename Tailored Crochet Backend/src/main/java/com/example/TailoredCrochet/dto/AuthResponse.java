package com.example.TailoredCrochet.dto;

public record AuthResponse(
        String accessToken,
        String email,
        String username
) {}