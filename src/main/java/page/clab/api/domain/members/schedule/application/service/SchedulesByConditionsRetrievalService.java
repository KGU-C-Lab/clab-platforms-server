package page.clab.api.domain.members.schedule.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.members.schedule.application.dto.mapper.ScheduleDtoMapper;
import page.clab.api.domain.members.schedule.application.dto.response.ScheduleResponseDto;
import page.clab.api.domain.members.schedule.application.port.in.RetrieveSchedulesByConditionsUseCase;
import page.clab.api.domain.members.schedule.application.port.out.RetrieveSchedulePort;
import page.clab.api.domain.members.schedule.domain.Schedule;
import page.clab.api.domain.members.schedule.domain.SchedulePriority;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class SchedulesByConditionsRetrievalService implements RetrieveSchedulesByConditionsUseCase {

    private final RetrieveSchedulePort retrieveSchedulePort;

    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<ScheduleResponseDto> retrieveSchedules(Integer year, Integer month, SchedulePriority priority, Pageable pageable) {
        Page<Schedule> schedules = retrieveSchedulePort.findByConditions(year, month, priority, pageable);
        return new PagedResponseDto<>(schedules.map(ScheduleDtoMapper::toScheduleResponseDto));
    }
}
