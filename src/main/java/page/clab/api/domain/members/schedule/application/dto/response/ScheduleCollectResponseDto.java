package page.clab.api.domain.members.schedule.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ScheduleCollectResponseDto {

    private Long totalScheduleCount;
    private Long totalEventCount;
}
