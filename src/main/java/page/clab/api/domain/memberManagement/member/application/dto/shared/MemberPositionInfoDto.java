package page.clab.api.domain.memberManagement.member.application.dto.shared;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberPositionInfoDto {

    private String memberId;
    private String memberName;
    private String email;
    private String imageUrl;
    private String interests;
    private String githubUrl;
}
