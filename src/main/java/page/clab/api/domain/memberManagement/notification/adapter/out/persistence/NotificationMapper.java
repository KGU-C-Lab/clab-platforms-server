package page.clab.api.domain.memberManagement.notification.adapter.out.persistence;

import org.mapstruct.Mapper;
import page.clab.api.domain.memberManagement.notification.domain.Notification;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationJpaEntity toEntity(Notification notification);

    Notification toDomain(NotificationJpaEntity jpaEntity);
}
