package page.clab.api.domain.activity.review.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReviewResponseDto {

    private Long id;
    private Long activityGroupId;
    private String activityGroupName;
    private String activityGroupCategory;
    private String memberId;
    private String name;
    private String department;
    private String content;
    private Boolean isPublic;
    private Boolean isOwner;
    private LocalDateTime createdAt;
}
