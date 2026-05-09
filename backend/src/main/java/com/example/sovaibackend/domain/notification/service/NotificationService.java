package com.sovai.platform.domain.notification.service;

import com.sovai.platform.common.response.PagedResponse;
import com.sovai.platform.domain.notification.dto.NotificationResponse;

public interface NotificationService {
    PagedResponse<NotificationResponse> list(Long userId, int page, int size);
    NotificationResponse markRead(Long id);
}
