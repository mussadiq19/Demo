package com.example.sovaibackend.domain.notification.service;

import com.example.sovaibackend.common.exception.ResourceNotFoundException;
import com.example.sovaibackend.common.response.PagedResponse;
import com.example.sovaibackend.domain.notification.dto.NotificationResponse;
import com.example.sovaibackend.domain.notification.entity.Notification;
import com.example.sovaibackend.domain.notification.mapper.NotificationMapper;
import com.example.sovaibackend.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public PagedResponse<NotificationResponse> list(Long userId, int page, int size) {
        return PagedResponse.from(notificationRepository.findByUserId(userId, PageRequest.of(page, size))
                .map(notificationMapper::toResponse));
    }

    @Override
    @Transactional
    public NotificationResponse markRead(Long id) {
        Notification n = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        n.setIsRead(true);
        return notificationMapper.toResponse(notificationRepository.save(n));
    }
}
