package page.clab.api.domain.memberManagement.notification.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.memberManagement.notification.domain.Notification;

import java.util.List;

public interface RetrieveNotificationPort {

    Notification getById(Long id);

    List<Notification> findByMemberId(String memberId);

    Page<Notification> findByMemberId(String memberId, Pageable pageable);
}
