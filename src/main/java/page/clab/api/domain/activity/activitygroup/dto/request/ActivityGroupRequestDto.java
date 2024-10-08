package page.clab.api.domain.activity.activitygroup.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupCategory;

import java.time.LocalDate;

@Getter
@Setter
public class ActivityGroupRequestDto {

    @NotNull(message = "{notnull.activityGroup.category}")
    private ActivityGroupCategory category;

    @NotNull(message = "{notnull.activityGroup.subject}")
    @Schema(description = "활동 대상", example = "1학년 이상")
    private String subject;

    @NotNull(message = "{notnull.activityGroup.name}")
    @Schema(description = "활동명", example = "2024-1 신입생 대상 C언어 스터디")
    private String name;

    @NotNull(message = "{notnull.activityGroup.content}")
    @Schema(description = "활동 설명", example = "2024-1 신입생 대상 C언어 스터디")
    private String content;

    @Schema(description = "활동 이미지 URL", example = "https://i.namu.wiki/i/KcqDuQYTxNpUcLIMZTg28QXse0XiWx1G7K68kYYCo1GuhoHmhB_V8Qe9odGGt0BH9-0nQZTN53WXTNpDmwVfWQ.svg")
    private String imageUrl;

    @Schema(description = "커리큘럼", example = "큐, 스택, 리스트, 연결리스트, 트리, 그래프")
    private String curriculum;

    @Schema(description = "활동 시작일", example = "2023-03-02")
    private LocalDate startDate;

    @Schema(description = "활동 종료일", example = "2023-06-18")
    private LocalDate endDate;

    @Schema(description = "기술 스택", example = "Unreal Engine, C#")
    private String techStack;

    @Schema(description = "Github URL", example = "https://github.com/KGU-C-Lab")
    private String githubUrl;
}
