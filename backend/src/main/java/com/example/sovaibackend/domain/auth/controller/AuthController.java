package com.sovai.platform.domain.auth.controller;

import com.sovai.platform.common.response.ApiResponse;
import com.sovai.platform.domain.auth.dto.AuthResponse;
import com.sovai.platform.domain.auth.dto.LoginRequest;
import com.sovai.platform.domain.auth.dto.RegisterRequest;
import com.sovai.platform.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ApiResponse<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ApiResponse.ok(authService.register(request), "Registered successfully with workforce-first setup");
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.ok(authService.login(request), "Login successful");
    }

    @PostMapping("/refresh")
    public ApiResponse<AuthResponse> refresh(@RequestHeader("Authorization") String bearerToken) {
        return ApiResponse.ok(authService.refreshToken(bearerToken), "Token refreshed successfully");
    }
}

