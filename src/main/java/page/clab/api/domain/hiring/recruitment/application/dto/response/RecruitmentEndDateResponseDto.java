package page.clab.api.domain.hiring.recruitment.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.hiring.application.domain.ApplicationType;

@Getter
@Builder
public class RecruitmentEndDateResponseDto {

    private Long id;
    private ApplicationType applicationType;
}
