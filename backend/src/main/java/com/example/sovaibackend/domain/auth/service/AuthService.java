package com.example.sovaibackend.domain.auth.service;

import com.example.sovaibackend.domain.auth.dto.AuthResponse;
import com.example.sovaibackend.domain.auth.dto.LoginRequest;
import com.example.sovaibackend.domain.auth.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse refreshToken(String bearerToken);
}

