package page.clab.api.domain.activity.activitygroup.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class AbsentResponseDto {

    private String absenteeId;
    private String absenteeName;
    private Long activityGroupId;
    private String activityGroupName;
    private String reason;
    private LocalDate absentDate;
}
