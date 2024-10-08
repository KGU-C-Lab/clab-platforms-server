package page.clab.api.domain.activity.activitygroup.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ActivityGroupBoardUpdateResponseDto {

    private Long id;
    private Long parentId;
}
