package page.clab.api.domain.memberManagement.notification.application.dto.mapper;

import page.clab.api.domain.memberManagement.notification.application.dto.response.NotificationResponseDto;
import page.clab.api.domain.memberManagement.notification.domain.Notification;

public class NotificationDtoMapper {

    public static NotificationResponseDto toNotificationResponseDto(Notification notification) {
        return NotificationResponseDto.builder()
                .id(notification.getId())
                .content(notification.getContent())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
