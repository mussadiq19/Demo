package com.example.sovaibackend.domain.notification.mapper;

import com.example.sovaibackend.domain.notification.dto.NotificationResponse;
import com.example.sovaibackend.domain.notification.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    @Mapping(target = "read", source = "isRead")
    @Mapping(target = "createdAt", expression = "java(notification.getCreatedAt().toString())")
    NotificationResponse toResponse(Notification notification);
}
