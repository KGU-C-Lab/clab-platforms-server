package page.clab.api.domain.memberManagement.notification.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.memberManagement.notification.application.port.out.RegisterNotificationPort;
import page.clab.api.domain.memberManagement.notification.application.port.out.RetrieveNotificationPort;
import page.clab.api.domain.memberManagement.notification.domain.Notification;
import page.clab.api.global.exception.NotFoundException;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationPersistenceAdapter implements
        RegisterNotificationPort,
        RetrieveNotificationPort {

    private final NotificationRepository repository;
    private final NotificationMapper mapper;

    @Override
    public Notification save(Notification notification) {
        NotificationJpaEntity entity = mapper.toEntity(notification);
        NotificationJpaEntity savedEntity = repository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public void saveAll(List<Notification> notifications) {
        List<NotificationJpaEntity> entities = notifications.stream()
                .map(mapper::toEntity)
                .toList();
        repository.saveAll(entities);
    }

    @Override
    public Notification getById(Long id) {
        return repository.findById(id)
                .map(mapper::toDomain)
                .orElseThrow(() -> new NotFoundException("[Notification] id: " + id + "에 해당하는 알림이 존재하지 않습니다."));
    }

    @Override
    public List<Notification> findByMemberId(String memberId) {
        return repository.findByMemberId(memberId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Page<Notification> findByMemberId(String memberId, Pageable pageable) {
        return repository.findByMemberId(memberId, pageable)
                .map(mapper::toDomain);
    }
}
