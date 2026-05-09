package com.sovai.platform.domain.notification.controller;

import com.sovai.platform.common.response.ApiResponse;
import com.sovai.platform.common.response.PagedResponse;
import com.sovai.platform.domain.notification.dto.NotificationResponse;
import com.sovai.platform.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ApiResponse<PagedResponse<NotificationResponse>> list(@RequestParam Long userId,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(notificationService.list(userId, page, size));
    }

    @PutMapping("/{id}/read")
    public ApiResponse<NotificationResponse> markRead(@PathVariable Long id) {
        return ApiResponse.ok(notificationService.markRead(id), "Notification marked as read");
    }
}
