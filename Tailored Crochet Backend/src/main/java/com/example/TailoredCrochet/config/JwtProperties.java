package com.example.TailoredCrochet.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String secret;
    private long expirationMs;

    public void setSecret(String secret) { this.secret = secret; }

    public void setExpirationMs(long expirationMs) { this.expirationMs = expirationMs; }
}