package page.clab.api.domain.activity.activitygroup.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupBoardCategory;
import page.clab.api.global.common.file.dto.response.UploadedFileResponseDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ActivityGroupBoardResponseDto {

    private Long id;
    private String memberId;
    private String memberName;
    private Long parentId;
    private ActivityGroupBoardCategory category;
    private String title;
    private String content;
    private List<UploadedFileResponseDto> files;
    private LocalDateTime dueDateTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
