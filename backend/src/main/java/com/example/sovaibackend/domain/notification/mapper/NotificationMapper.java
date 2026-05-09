package com.example.sovaibackend.domain.notification.mapper;

import com.example.sovaibackend.domain.notification.dto.NotificationResponse;
import com.example.sovaibackend.domain.notification.entity.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    NotificationResponse toResponse(Notification notification);
}

