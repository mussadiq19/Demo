package com.example.sovaibackend.domain.auth.dto;

public record AuthResponse(
    String token,
    Long userId,
    String role,
    long expiresAt
) {
}

