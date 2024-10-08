package page.clab.api.domain.memberManagement.member.application.dto.shared;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberDetailedInfoDto {

    private String memberId;
    private String memberName;
    private Long roleLevel;
    private String imageUrl;
    private boolean isGraduated;

    public boolean isAdminRole() {
        return roleLevel >= 2;
    }

    public boolean isSuperAdminRole() {
        return roleLevel == 3;
    }
}
