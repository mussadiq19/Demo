package com.example.sovaibackend.domain.auth.service;

import com.example.sovaibackend.common.exception.ResourceNotFoundException;
import com.example.sovaibackend.common.exception.UnauthorizedException;
import com.example.sovaibackend.common.security.JwtTokenProvider;
import com.example.sovaibackend.domain.auth.dto.AuthResponse;
import com.example.sovaibackend.domain.auth.dto.LoginRequest;
import com.example.sovaibackend.domain.auth.dto.RegisterRequest;
import com.example.sovaibackend.domain.auth.entity.Role;
import com.example.sovaibackend.domain.auth.entity.User;
import com.example.sovaibackend.domain.auth.repository.UserRepository;
import com.example.sovaibackend.domain.company.entity.Company;
import com.example.sovaibackend.domain.company.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        var company = companyRepository.save(Company.builder()
                .name("Default Company")
                .build());

        var user = userRepository.save(User.builder()
                .email(request.email())
                .fullName(request.fullName())
                .passwordHash(passwordEncoder.encode(request.password()))
                .role(Role.COMPANY_ADMIN)
                .companyId(company.getId())
                .build());

        String token = jwtTokenProvider.generateToken(user.getId(), user.getCompanyId(), user.getRole().name());
        return toAuthResponse(user, token);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        var user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        String token = jwtTokenProvider.generateToken(user.getId(), user.getCompanyId(), user.getRole().name());
        return toAuthResponse(user, token);
    }

    @Override
    public AuthResponse refreshToken(String bearerToken) {
        String token = bearerToken.replace("Bearer ", "").trim();
        if (!jwtTokenProvider.validateToken(token)) {
            throw new UnauthorizedException("Token is invalid or expired");
        }
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        String refreshed = jwtTokenProvider.generateToken(user.getId(), user.getCompanyId(), user.getRole().name());
        return toAuthResponse(user, refreshed);
    }

    private AuthResponse toAuthResponse(User user, String token) {
        long expiresAt = Instant.now().toEpochMilli() + expirationMs;
        return new AuthResponse(
            token,
            user.getId(),
            user.getRole().name(),
            expiresAt
        );
    }
}

