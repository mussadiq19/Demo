package com.example.sovaibackend.domain.auth.controller;

import com.example.sovaibackend.common.response.ApiResponse;
import com.example.sovaibackend.common.security.UserPrincipal;
import com.example.sovaibackend.domain.auth.dto.AuthResponse;
import com.example.sovaibackend.domain.auth.dto.LoginRequest;
import com.example.sovaibackend.domain.auth.dto.RegisterRequest;
import com.example.sovaibackend.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
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

    /**
     * Get current authenticated user information
     * @param authentication The authentication object from Spring Security
     * @return User details from the JWT token
     */
    @GetMapping("/me")
    public ApiResponse<UserPrincipal> getCurrentUser(Authentication authentication) {
        // Authentication is automatically populated from JWT by the JwtAuthenticationFilter
        if (authentication == null || !authentication.isAuthenticated()) {
            return ApiResponse.error("User not authenticated");
        }

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return ApiResponse.ok(principal, "Current user retrieved successfully");
    }

    /**
     * Logout endpoint - client-side handles token removal
     * This endpoint can be used for additional cleanup if needed
     */
    @PostMapping("/logout")
    public ApiResponse<Void> logout() {
        // Client will remove token from localStorage
        // Backend can perform additional cleanup here if needed
        return ApiResponse.ok(null, "Logout successful");
    }
}
