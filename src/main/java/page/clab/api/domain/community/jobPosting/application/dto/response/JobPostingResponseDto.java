package page.clab.api.domain.community.jobPosting.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class JobPostingResponseDto {

    private Long id;
    private String title;
    private String recruitmentPeriod;
    private String jobPostingUrl;
    private LocalDateTime createdAt;
}
