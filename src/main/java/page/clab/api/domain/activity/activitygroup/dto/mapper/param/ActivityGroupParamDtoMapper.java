package page.clab.api.domain.activity.activitygroup.dto.mapper.param;

import page.clab.api.domain.activity.activitygroup.domain.ActivityGroup;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupBoard;
import page.clab.api.domain.activity.activitygroup.domain.GroupMember;
import page.clab.api.domain.activity.activitygroup.domain.GroupSchedule;
import page.clab.api.domain.activity.activitygroup.dto.param.ActivityGroupDetails;
import page.clab.api.domain.activity.activitygroup.dto.param.GroupScheduleDto;

import java.util.List;

public class ActivityGroupParamDtoMapper {

    public static ActivityGroupDetails toActivityGroupDetails(ActivityGroup activityGroup, List<GroupMember> groupMembers, List<ActivityGroupBoard> activityGroupBoards) {
        return new ActivityGroupDetails(activityGroup, groupMembers, activityGroupBoards);
    }

    public static GroupScheduleDto toGroupScheduleDto(GroupSchedule groupSchedule) {
        return GroupScheduleDto.builder()
                .schedule(groupSchedule.getSchedule())
                .content(groupSchedule.getContent())
                .build();
    }

    public static GroupSchedule toGroupSchedule(GroupScheduleDto groupScheduleDto, ActivityGroup activityGroup) {
        return GroupSchedule.builder()
                .activityGroup(activityGroup)
                .schedule(groupScheduleDto.getSchedule())
                .content(groupScheduleDto.getContent())
                .build();
    }
}
