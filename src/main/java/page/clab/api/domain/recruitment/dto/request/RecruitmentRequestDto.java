package page.clab.api.domain.recruitment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.application.domain.ApplicationType;
import page.clab.api.domain.recruitment.domain.Recruitment;

import java.time.LocalDateTime;

@Getter
@Setter
public class RecruitmentRequestDto {

    @NotNull(message = "{notNull.recruitment.startDate}")
    @Schema(description = "모집 시작일", example = "2023-11-06T00:00:00", required = true)
    private LocalDateTime startDate;

    @NotNull(message = "{notNull.recruitment.endDate}")
    @Schema(description = "모집 종료일", example = "2023-11-08T00:00:00", required = true)
    private LocalDateTime endDate;

    @NotNull(message = "{notNull.recruitment.applicationType}")
    @Schema(description = "구분", example = "CORE_TEAM", required = true)
    private ApplicationType applicationType;

    @NotNull(message = "{notNull.recruitment.target}")
    @Schema(description = "대상", example = "2~3학년", required = true)
    private String target;

    @NotNull(message = "{notNull.recruitment.status}")
    @Schema(description = "상태", example = "종료", required = true)
    private String status;

    public static Recruitment toEntity(RecruitmentRequestDto requestDto) {
        return Recruitment.builder()
                .startDate(requestDto.getStartDate())
                .endDate(requestDto.getEndDate())
                .applicationType(requestDto.getApplicationType())
                .target(requestDto.getTarget())
                .status(requestDto.getStatus())
                .build();
    }

}
