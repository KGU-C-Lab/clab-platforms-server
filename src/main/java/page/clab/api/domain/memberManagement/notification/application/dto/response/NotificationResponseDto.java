package page.clab.api.domain.memberManagement.notification.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationResponseDto {

    private Long id;
    private String content;
    private LocalDateTime createdAt;
}
