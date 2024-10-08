package page.clab.api.domain.community.accuse.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.community.accuse.domain.AccuseStatus;
import page.clab.api.domain.community.accuse.domain.TargetType;

import java.time.LocalDateTime;

@Getter
@Builder
public class AccuseMyResponseDto {

    private TargetType targetType;
    private Long targetId;
    private String reason;
    private AccuseStatus accuseStatus;
    private LocalDateTime createdAt;
}
