package com.example.sovaibackend.domain.auth.controller;

import com.example.sovaibackend.common.response.ApiResponse;
import com.example.sovaibackend.domain.auth.dto.AuthResponse;
import com.example.sovaibackend.domain.auth.dto.LoginRequest;
import com.example.sovaibackend.domain.auth.dto.RegisterRequest;
import com.example.sovaibackend.domain.auth.service.AuthService;
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

