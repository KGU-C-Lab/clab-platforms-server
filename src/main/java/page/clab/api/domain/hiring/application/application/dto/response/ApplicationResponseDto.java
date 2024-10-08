package page.clab.api.domain.hiring.application.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.hiring.application.domain.ApplicationType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class ApplicationResponseDto {

    private String studentId;
    private Long recruitmentId;
    private String name;
    private String contact;
    private String email;
    private String department;
    private Long grade;
    private LocalDate birth;
    private String address;
    private String interests;
    private String otherActivities;
    private String githubUrl;
    private ApplicationType applicationType;
    private Boolean isPass;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
}
