package page.clab.api.domain.activity.activitygroup.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AttendanceResponseDto {

    private Long activityGroupId;
    private String memberId;
    private LocalDateTime attendanceDateTime;
}
