package page.clab.api.domain.memberManagement.member.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.memberManagement.member.domain.Role;

@Getter
@Builder
public class MemberRoleInfoResponseDto {

    private String id;
    private String name;
    private Role role;
}
