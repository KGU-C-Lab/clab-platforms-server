package page.clab.api.domain.members.schedule.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.members.schedule.domain.SchedulePriority;

import java.time.LocalDateTime;

@Getter
@Builder
public class ScheduleResponseDto {

    private Long id;
    private String title;
    private String detail;
    private String activityName;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private SchedulePriority priority;
}
