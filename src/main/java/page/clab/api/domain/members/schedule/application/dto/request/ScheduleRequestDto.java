package page.clab.api.domain.members.schedule.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.members.schedule.domain.SchedulePriority;
import page.clab.api.domain.members.schedule.domain.ScheduleType;

import java.time.LocalDateTime;

@Getter
@Setter
public class ScheduleRequestDto {

    @NotNull(message = "{notNull.schedule.scheduleType}")
    @Schema(description = "일정 타입", example = "ALL")
    private ScheduleType scheduleType;

    @NotNull(message = "{notNull.schedule.title}")
    @Schema(description = "일정 제목", example = "동아리 개강총회")
    private String title;

    @NotNull(message = "{notNull.schedule.detail}")
    @Schema(description = "일정 세부사항", example = "학기를 C-Lab과 함께 시작할 수 있는 자리!")
    private String detail;

    @NotNull(message = "{notNull.schedule.startDate}")
    @Schema(description = "일정 시작날짜와 시간", example = "2023-11-28 18:00:00.000")
    private LocalDateTime startDateTime;

    @NotNull(message = "{notNull.schedule.endDate}")
    @Schema(description = "일정 종료날짜와 시간", example = "2023-11-28 22:00:00.000")
    private LocalDateTime endDateTime;

    @NotNull(message = "{notNull.schedule.priority}")
    @Schema(description = "일정 중요도", example = "HIGH")
    private SchedulePriority priority;

    private Long activityGroupId;
}
