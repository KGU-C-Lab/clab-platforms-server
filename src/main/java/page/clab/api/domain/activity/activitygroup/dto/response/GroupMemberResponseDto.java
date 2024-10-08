package page.clab.api.domain.activity.activitygroup.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.activity.activitygroup.domain.GroupMemberStatus;

@Getter
@Builder
public class GroupMemberResponseDto {

    private String memberId;
    private String memberName;
    private String role;
    private GroupMemberStatus status;
}
