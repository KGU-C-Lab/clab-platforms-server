package page.clab.api.domain.award.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.award.application.dto.response.AwardResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveMyAwardsUseCase {
    PagedResponseDto<AwardResponseDto> retrieveMyAwards(Pageable pageable);
}
