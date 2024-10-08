package page.clab.api.domain.members.schedule.application.dto.mapper;

import page.clab.api.domain.activity.activitygroup.domain.ActivityGroup;
import page.clab.api.domain.members.schedule.application.dto.request.ScheduleRequestDto;
import page.clab.api.domain.members.schedule.application.dto.response.ScheduleCollectResponseDto;
import page.clab.api.domain.members.schedule.application.dto.response.ScheduleResponseDto;
import page.clab.api.domain.members.schedule.domain.Schedule;

public class ScheduleDtoMapper {

    public static Schedule toSchedule(ScheduleRequestDto requestDto, String memberId, ActivityGroup activityGroup) {
        return Schedule.builder()
                .scheduleType(requestDto.getScheduleType())
                .title(requestDto.getTitle())
                .detail(requestDto.getDetail())
                .startDateTime(requestDto.getStartDateTime())
                .endDateTime(requestDto.getEndDateTime())
                .priority(requestDto.getPriority())
                .scheduleWriter(memberId)
                .activityGroup(activityGroup)
                .isDeleted(false)
                .build();
    }

    public static ScheduleCollectResponseDto toScheduleCollectResponseDto(Long totalScheduleCount, Long totalEventCount) {
        return ScheduleCollectResponseDto.builder()
                .totalScheduleCount(totalScheduleCount)
                .totalEventCount(totalEventCount)
                .build();
    }

    public static ScheduleResponseDto toScheduleResponseDto(Schedule schedule) {
        return ScheduleResponseDto.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .detail(schedule.getDetail())
                .activityName(schedule.isAllSchedule() ? null : schedule.getActivityGroup().getName())
                .startDateTime(schedule.getStartDateTime())
                .endDateTime(schedule.getEndDateTime())
                .priority(schedule.getPriority())
                .build();
    }
}
