package page.clab.api.domain.auth.blacklistIp.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BlacklistIpResponseDto {

    private Long id;
    private String ipAddress;
    private String reason;
    private LocalDateTime createdAt;
}
