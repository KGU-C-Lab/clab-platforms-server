package page.clab.api.domain.activity.activitygroup.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupCategory;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupStatus;

@Getter
@Builder
public class ActivityGroupDetailResponseDto {

    private Long id;
    private ActivityGroupCategory category;
    private String subject;
    private String name;
    private String content;
    private ActivityGroupStatus status;
    private Long progress;
    private String imageUrl;
    private String curriculum;
    private List<GroupMemberResponseDto> groupMembers;
    private LocalDate startDate;
    private LocalDate endDate;
    private String techStack;
    private String githubUrl;
    private List<ActivityGroupBoardResponseDto> activityGroupBoards;

    @JsonProperty("isOwner")
    private boolean isOwner;
    private LocalDateTime createdAt;
}
