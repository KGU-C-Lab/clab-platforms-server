package page.clab.api.domain.award.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.award.application.port.in.RetrieveAwardsUseCase;
import page.clab.api.domain.award.application.port.out.RetrieveAwardPort;
import page.clab.api.domain.award.domain.Award;
import page.clab.api.domain.award.dto.response.AwardResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class AwardRetrievalService implements RetrieveAwardsUseCase {

    private final RetrieveAwardPort retrieveAwardPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<AwardResponseDto> retrieveAwards(String memberId, Long year, Pageable pageable) {
        Page<Award> awards = retrieveAwardPort.findByConditions(memberId, year, pageable);
        return new PagedResponseDto<>(awards.map(AwardResponseDto::toDto));
    }
}
