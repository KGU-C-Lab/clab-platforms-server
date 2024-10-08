package page.clab.api.domain.activity.activitygroup.dto.param;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class GroupScheduleDto {

    private LocalDateTime schedule;
    private String content;
}
