package page.clab.api.domain.members.schedule.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.activity.activitygroup.application.ActivityGroupMemberService;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroup;
import page.clab.api.domain.activity.activitygroup.domain.GroupMember;
import page.clab.api.domain.members.schedule.application.dto.mapper.ScheduleDtoMapper;
import page.clab.api.domain.members.schedule.application.dto.response.ScheduleResponseDto;
import page.clab.api.domain.members.schedule.application.port.in.RetrieveSchedulesWithinDateRangeUseCase;
import page.clab.api.domain.members.schedule.application.port.out.RetrieveSchedulePort;
import page.clab.api.domain.members.schedule.domain.Schedule;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.common.dto.PagedResponseDto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SchedulesWithinDateRangeRetrievalService implements RetrieveSchedulesWithinDateRangeUseCase {

    private final RetrieveSchedulePort retrieveSchedulePort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final ActivityGroupMemberService activityGroupMemberService;

    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<ScheduleResponseDto> retrieveSchedulesWithinDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        String currentMemberId = externalRetrieveMemberUseCase.getCurrentMemberId();
        List<GroupMember> groupMembers = activityGroupMemberService.getGroupMemberByMemberId(currentMemberId);
        List<ActivityGroup> myGroups = getMyActivityGroups(groupMembers);
        Page<Schedule> schedules = retrieveSchedulePort.findByDateRangeAndMember(startDate, endDate, myGroups, pageable);
        return new PagedResponseDto<>(schedules.map(ScheduleDtoMapper::toScheduleResponseDto));
    }

    private List<ActivityGroup> getMyActivityGroups(List<GroupMember> groupMembers) {
        return groupMembers.stream()
                .map(GroupMember::getActivityGroup)
                .collect(Collectors.toList());
    }
}
