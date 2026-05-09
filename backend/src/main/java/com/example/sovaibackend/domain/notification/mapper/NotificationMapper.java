package com.sovai.platform.domain.notification.mapper;

import com.sovai.platform.domain.notification.dto.NotificationResponse;
import com.sovai.platform.domain.notification.entity.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    NotificationResponse toResponse(Notification notification);
}

