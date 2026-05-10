package com.example.sovaibackend.domain.notification.service;

import com.example.sovaibackend.domain.notification.dto.NotificationResponse;

import java.util.List;

public interface NotificationService {
    List<NotificationResponse> getByUserId(Long userId);
    void markAsRead(Long id);
}
