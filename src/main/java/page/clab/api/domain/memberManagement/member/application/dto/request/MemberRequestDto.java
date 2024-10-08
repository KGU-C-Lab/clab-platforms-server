package page.clab.api.domain.memberManagement.member.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.memberManagement.member.domain.StudentStatus;

import java.time.LocalDate;

@Getter
@Setter
public class MemberRequestDto {

    @NotNull(message = "{notNull.member.id}")
    @Schema(description = "학번", example = "202310000", required = true)
    private String id;

    @Schema(description = "비밀번호", example = "1234")
    private String password;

    @NotNull(message = "{notNull.member.name}")
    @Schema(description = "이름", example = "홍길동", required = true)
    private String name;

    @NotNull(message = "{notNull.member.contact}")
    @Schema(description = "연락처", example = "01012345678", required = true)
    private String contact;

    @NotNull(message = "{notNull.member.email}")
    @Schema(description = "이메일", example = "clab.coreteam@gamil.com", required = true)
    private String email;

    @NotNull(message = "{notNull.member.department}")
    @Schema(description = "학과", example = "AI컴퓨터공학부", required = true)
    private String department;

    @NotNull(message = "{notNull.member.grade}")
    @Schema(description = "학년", example = "1", required = true)
    private Long grade;

    @NotNull(message = "{notNull.member.birth}")
    @Schema(description = "생년월일", example = "2004-01-01", required = true)
    private LocalDate birth;

    @NotNull(message = "{notNull.member.address}")
    @Schema(description = "주소", example = "경기도 수원시 영통구 광교산로 154-42", required = true)
    private String address;

    @NotNull(message = "{notNull.member.interests}")
    @Schema(description = "관심 분야", example = "백엔드", required = true)
    private String interests;

    @Schema(description = "GitHub 주소", example = "https://github.com/kgu-c-lab", required = true)
    private String githubUrl;

    @NotNull(message = "{notNull.member.studentStatus}")
    @Schema(description = "학적", example = "CURRENT", required = true)
    private StudentStatus studentStatus;

    @Schema(description = "프로필 이미지", example = "https://www.clab.page/assets/dongmin-860f3a1e.jpeg")
    private String imageUrl;
}
