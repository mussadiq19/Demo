package com.sovai.platform.domain.notification.dto;

import java.time.Instant;

public record NotificationResponse(
    Long id,
    Long userId,
    String type,
    String message,
    Boolean isRead,
    Instant createdAt
) {
}

