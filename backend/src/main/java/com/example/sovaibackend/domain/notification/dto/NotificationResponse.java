package com.example.sovaibackend.domain.notification.dto;

public record NotificationResponse(Long id, String type, String message, boolean read, String createdAt) {}
