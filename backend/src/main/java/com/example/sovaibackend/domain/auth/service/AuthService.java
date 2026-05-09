package com.sovai.platform.domain.auth.service;

import com.sovai.platform.domain.auth.dto.AuthResponse;
import com.sovai.platform.domain.auth.dto.LoginRequest;
import com.sovai.platform.domain.auth.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse refreshToken(String bearerToken);
}

