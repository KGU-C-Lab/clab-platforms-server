package page.clab.api.domain.memberManagement.member.application.dto.shared;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.memberManagement.member.domain.Role;

@Getter
@Builder
public class MemberLoginInfoDto {

    private String memberId;
    private String memberName;
    private Role role;
    private boolean isOtpEnabled;

    public boolean isAdminRole() {
        return role.toRoleLevel() >= 2;
    }

    public boolean isSuperAdminRole() {
        return role.toRoleLevel() == 3;
    }
}
