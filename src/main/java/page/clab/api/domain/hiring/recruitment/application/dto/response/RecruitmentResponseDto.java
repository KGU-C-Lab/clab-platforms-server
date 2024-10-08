package page.clab.api.domain.hiring.recruitment.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.hiring.application.domain.ApplicationType;

import java.time.LocalDateTime;

@Getter
@Builder
public class RecruitmentResponseDto {

    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private ApplicationType applicationType;
    private String target;
    private String status;
    private LocalDateTime updatedAt;
}
