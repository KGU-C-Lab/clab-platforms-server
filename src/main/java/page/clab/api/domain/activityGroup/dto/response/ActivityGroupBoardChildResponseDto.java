package page.clab.api.domain.activityGroup.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.activityGroup.domain.ActivityGroupBoard;
import page.clab.api.domain.activityGroup.domain.ActivityGroupBoardCategory;
import page.clab.api.global.common.file.dto.response.AssignmentFileResponseDto;
import page.clab.api.global.util.ModelMapperUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityGroupBoardChildResponseDto {

    private Long id;

    private ActivityGroupBoardCategory category;

    private String title;

    private String content;

    private LocalDateTime dueDateTime;

    private List<AssignmentFileResponseDto> assignmentFiles = new ArrayList<>();

    private List<ActivityGroupBoardChildResponseDto> children;

    public static ActivityGroupBoardChildResponseDto of(ActivityGroupBoard activityGroupBoard) {
        return ModelMapperUtil.getModelMapper().map(activityGroupBoard, ActivityGroupBoardChildResponseDto.class);
    }

}
