package page.clab.api.domain.memberManagement.member.application.dto.shared;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberBasicInfoDto {

    private String memberId;
    private String memberName;
}
