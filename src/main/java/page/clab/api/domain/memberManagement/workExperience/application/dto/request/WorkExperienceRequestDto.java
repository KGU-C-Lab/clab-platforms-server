package page.clab.api.domain.memberManagement.workExperience.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class WorkExperienceRequestDto {

    @NotNull(message = "{notNull.workExperience.companyName}")
    @Schema(description = "회사명", example = "네이버 클라우드", required = true)
    private String companyName;

    @NotNull(message = "{notNull.workExperience.position}")
    @Schema(description = "직책", example = "인턴", required = true)
    private String position;

    @NotNull(message = "{notNull.workExperience.startDate}")
    @Schema(description = "시작일", example = "2023-01-01", required = true)
    private LocalDate startDate;

    @NotNull(message = "{notNull.workExperience.endDate}")
    @Schema(description = "종료일", example = "2023-12-31", required = true)
    private LocalDate endDate;
}
