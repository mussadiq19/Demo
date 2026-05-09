package com.example.sovaibackend.domain.notification.service;

import com.example.sovaibackend.common.response.PagedResponse;
import com.example.sovaibackend.domain.notification.dto.NotificationResponse;

public interface NotificationService {
    PagedResponse<NotificationResponse> list(Long userId, int page, int size);
    NotificationResponse markRead(Long id);
}
