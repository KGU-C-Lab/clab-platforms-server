package page.clab.api.domain.community.accuse.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.community.accuse.domain.AccuseStatus;
import page.clab.api.domain.community.accuse.domain.TargetType;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBasicInfoDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class AccuseResponseDto {

    private List<MemberBasicInfoDto> members;
    private TargetType targetType;
    private Long targetId;
    private String reason;
    private AccuseStatus accuseStatus;
    private Long accuseCount;
    private LocalDateTime createdAt;
}
