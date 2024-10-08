package page.clab.api.domain.members.schedule.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroup;
import page.clab.api.domain.members.schedule.application.dto.response.ScheduleCollectResponseDto;
import page.clab.api.domain.members.schedule.domain.Schedule;
import page.clab.api.domain.members.schedule.domain.SchedulePriority;

import java.time.LocalDate;
import java.util.List;

public interface RetrieveSchedulePort {

    Schedule getById(Long id);

    Page<Schedule> findByConditions(Integer year, Integer month, SchedulePriority priority, Pageable pageable);

    Page<Schedule> findActivitySchedulesByDateRangeAndMemberId(LocalDate startDate, LocalDate endDate, String memberId, Pageable pageable);

    ScheduleCollectResponseDto findCollectSchedules();

    Page<Schedule> findByDateRangeAndMember(LocalDate startDate, LocalDate endDate, List<ActivityGroup> myGroups, Pageable pageable);
}
