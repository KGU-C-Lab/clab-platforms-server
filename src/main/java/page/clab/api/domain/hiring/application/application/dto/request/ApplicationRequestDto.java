package page.clab.api.domain.hiring.application.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.hiring.application.domain.ApplicationType;

import java.time.LocalDate;

@Getter
@Setter
public class ApplicationRequestDto {

    @NotNull(message = "{notNull.application.studentId}")
    @Schema(description = "학번", example = "202312000", required = true)
    private String studentId;

    @NotNull(message = "{notNull.application.recruitmentId}")
    @Schema(description = "모집 일정 ID", example = "1", required = true)
    private Long recruitmentId;

    @NotNull(message = "{notNull.application.name}")
    @Schema(description = "이름", example = "홍길동", required = true)
    private String name;

    @NotNull(message = "{notNull.application.contact}")
    @Schema(description = "연락처", example = "01012345678", required = true)
    private String contact;

    @NotNull(message = "{notNull.application.email}")
    @Schema(description = "이메일", example = "clab.coreteam@gamil.com", required = true)
    private String email;

    @NotNull(message = "{notNull.application.department}")
    @Schema(description = "학과", example = "AI컴퓨터공학부", required = true)
    private String department;

    @NotNull(message = "{notNull.application.grade}")
    @Schema(description = "학년", example = "1", required = true)
    private Long grade;

    @NotNull(message = "{notNull.application.birth}")
    @Schema(description = "생년월일", example = "2004-01-01", required = true)
    private LocalDate birth;

    @NotNull(message = "{notNull.application.address}")
    @Schema(description = "주소", example = "경기도 수원시 영통구 광교산로 154-42", required = true)
    private String address;

    @NotNull(message = "{notNull.application.interests}")
    @Schema(description = "관심분야", example = "백엔드", required = true)
    private String interests;

    @NotNull(message = "{notNull.application.otherActivities}")
    @Schema(description = "IT 관련 자격증 및 활동", example = "경기대학교 컴퓨터공학과 학생회", required = true)
    private String otherActivities;

    @Schema(description = "GitHub", example = "https://github.com/KGU-C-Lab")
    private String githubUrl;

    @NotNull(message = "{notNull.application.applicationType}")
    @Schema(description = "구분", example = "NORMAL", required = true)
    private ApplicationType applicationType;
}
