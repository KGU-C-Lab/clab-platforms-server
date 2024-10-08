package page.clab.api.domain.memberManagement.award.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class AwardResponseDto {

    private Long id;
    private String competitionName;
    private String organizer;
    private String awardName;
    private LocalDate awardDate;
}
