package page.clab.api.domain.members.schedule.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroup;
import page.clab.api.domain.members.schedule.application.dto.response.ScheduleCollectResponseDto;
import page.clab.api.domain.members.schedule.application.port.out.RegisterSchedulePort;
import page.clab.api.domain.members.schedule.application.port.out.RetrieveSchedulePort;
import page.clab.api.domain.members.schedule.domain.Schedule;
import page.clab.api.domain.members.schedule.domain.SchedulePriority;
import page.clab.api.global.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SchedulePersistenceAdapter implements
        RegisterSchedulePort,
        RetrieveSchedulePort {

    private final ScheduleRepository repository;
    private final ScheduleMapper mapper;

    @Override
    public Schedule save(Schedule schedule) {
        ScheduleJpaEntity entity = mapper.toEntity(schedule);
        ScheduleJpaEntity savedEntity = repository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Schedule getById(Long id) {
        return repository.findById(id)
                .map(mapper::toDomain)
                .orElseThrow(() -> new NotFoundException("[Schedule] id: " + id + "에 해당하는 스케줄이 존재하지 않습니다."));
    }

    @Override
    public Page<Schedule> findActivitySchedulesByDateRangeAndMemberId(LocalDate startDate, LocalDate endDate, String memberId, Pageable pageable) {
        return repository.findActivitySchedulesByDateRangeAndMemberId(startDate, endDate, memberId, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public ScheduleCollectResponseDto findCollectSchedules() {
        return repository.findCollectSchedules();
    }

    @Override
    public Page<Schedule> findByConditions(Integer year, Integer month, SchedulePriority priority, Pageable pageable) {
        return repository.findByConditions(year, month, priority, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Page<Schedule> findByDateRangeAndMember(LocalDate startDate, LocalDate endDate, List<ActivityGroup> myGroups, Pageable pageable) {
        return repository.findByDateRangeAndMember(startDate, endDate, myGroups, pageable)
                .map(mapper::toDomain);
    }
}
