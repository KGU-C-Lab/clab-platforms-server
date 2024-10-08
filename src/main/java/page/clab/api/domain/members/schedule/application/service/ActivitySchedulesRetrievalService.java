package page.clab.api.domain.members.schedule.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.members.schedule.application.dto.mapper.ScheduleDtoMapper;
import page.clab.api.domain.members.schedule.application.dto.response.ScheduleResponseDto;
import page.clab.api.domain.members.schedule.application.port.in.RetrieveActivitySchedulesUseCase;
import page.clab.api.domain.members.schedule.application.port.out.RetrieveSchedulePort;
import page.clab.api.domain.members.schedule.domain.Schedule;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.common.dto.PagedResponseDto;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ActivitySchedulesRetrievalService implements RetrieveActivitySchedulesUseCase {

    private final RetrieveSchedulePort retrieveSchedulePort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;

    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<ScheduleResponseDto> retrieveActivitySchedules(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        String currentMemberId = externalRetrieveMemberUseCase.getCurrentMemberId();
        Page<Schedule> schedules = retrieveSchedulePort.findActivitySchedulesByDateRangeAndMemberId(startDate, endDate, currentMemberId, pageable);
        return new PagedResponseDto<>(schedules.map(ScheduleDtoMapper::toScheduleResponseDto));
    }
}
