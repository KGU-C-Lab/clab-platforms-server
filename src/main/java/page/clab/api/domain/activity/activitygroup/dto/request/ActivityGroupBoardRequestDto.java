package page.clab.api.domain.activity.activitygroup.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupBoardCategory;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ActivityGroupBoardRequestDto {

    @NotNull(message = "{notNull.activityGroupBoard.category}")
    @Schema(description = "카테고리", example = "NOTICE")
    private ActivityGroupBoardCategory category;

    @Schema(description = "제목", example = "C언어 스터디 과제 제출 관련 공지")
    private String title;

    @Schema(description = "내용", example = "C언어 스터디 과제 제출 관련 공지")
    private String content;

    @Schema(description = "첨부파일 경로 리스트", example = "[\"/resources/files/assignment/1/1/superuser/339609571877700_4305d83e-090a-480b-a470-b5e96164d113.png\", \"/resources/files/assignment/2/1/superuser/4305d83e-090a-480b-a470-b5e96164d114.png\"]")
    private List<String> fileUrls;

    @Schema(description = "마감일자", example = "2024-11-28 18:00:00.000")
    private LocalDateTime dueDateTime;
}
