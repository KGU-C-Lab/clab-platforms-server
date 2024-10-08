package page.clab.api.domain.activity.activitygroup.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivityGroupReportRequestDto {

    @NotNull(message = "notNull.report.turn")
    @Schema(description = "차시", example = "1", required = true)
    private Long turn;

    @NotNull(message = "notNull.report.activityGroupId")
    @Schema(description = "활동그룹 아이디", example = "1", required = true)
    private Long activityGroupId;

    @NotNull(message = "notNull.report.title")
    @Schema(description = "제목", example = "C언어 기초 1차시 보고서", required = true)
    private String title;

    @NotNull(message = "notNull.report.content")
    @Schema(description = "내용", example = "변수, 자료형에 대해 공부", required = true)
    private String content;
}
