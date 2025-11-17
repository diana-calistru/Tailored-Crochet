package com.example.TailoredCrochet.security;

import com.example.TailoredCrochet.config.JwtProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtUtils {
    private final String jwtSecret;
    private final long jwtExpirationMs;

    public JwtUtils(JwtProperties jwtProperties) {
        this.jwtSecret = jwtProperties.getSecret();
        this.jwtExpirationMs = jwtProperties.getExpirationMs();
        if (jwtSecret.length() < 64) { // 512 bits = 64 bytes
            throw new IllegalArgumentException("JWT secret must be at least 512 bits");
        }
    }
    public String generateJwtToken(String username) {
        // Convert secret string to proper key
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key)
                .compact();
    }
}