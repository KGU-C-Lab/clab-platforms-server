package page.clab.api.domain.memberManagement.notification.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.notification.application.dto.mapper.NotificationDtoMapper;
import page.clab.api.domain.memberManagement.notification.application.dto.response.NotificationResponseDto;
import page.clab.api.domain.memberManagement.notification.application.port.in.RetrieveNotificationsUseCase;
import page.clab.api.domain.memberManagement.notification.application.port.out.RetrieveNotificationPort;
import page.clab.api.domain.memberManagement.notification.domain.Notification;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class NotificationsRetrievalService implements RetrieveNotificationsUseCase {

    private final RetrieveNotificationPort retrieveNotificationPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<NotificationResponseDto> retrieveNotifications(Pageable pageable) {
        String currentMemberId = externalRetrieveMemberUseCase.getCurrentMemberId();
        Page<Notification> notifications = retrieveNotificationPort.findByMemberId(currentMemberId, pageable);
        return new PagedResponseDto<>(notifications.map(NotificationDtoMapper::toNotificationResponseDto));
    }
}
