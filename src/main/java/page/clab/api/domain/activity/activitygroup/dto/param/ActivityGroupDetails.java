package page.clab.api.domain.activity.activitygroup.dto.param;

import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroup;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupBoard;
import page.clab.api.domain.activity.activitygroup.domain.GroupMember;

import java.util.List;

@Getter
@Setter
public class ActivityGroupDetails {

    private ActivityGroup activityGroup;
    private List<GroupMember> groupMembers;
    private List<ActivityGroupBoard> activityGroupBoards;

    public ActivityGroupDetails(ActivityGroup activityGroup, List<GroupMember> groupMembers, List<ActivityGroupBoard> activityGroupBoards) {
        this.activityGroup = activityGroup;
        this.groupMembers = groupMembers;
        this.activityGroupBoards = activityGroupBoards;
    }
}
