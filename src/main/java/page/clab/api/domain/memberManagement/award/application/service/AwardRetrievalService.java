package page.clab.api.domain.memberManagement.award.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.award.application.dto.mapper.AwardDtoMapper;
import page.clab.api.domain.memberManagement.award.application.dto.response.AwardResponseDto;
import page.clab.api.domain.memberManagement.award.application.port.in.RetrieveAwardsUseCase;
import page.clab.api.domain.memberManagement.award.application.port.out.RetrieveAwardPort;
import page.clab.api.domain.memberManagement.award.domain.Award;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class AwardRetrievalService implements RetrieveAwardsUseCase {

    private final RetrieveAwardPort retrieveAwardPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<AwardResponseDto> retrieveAwards(String memberId, Long year, Pageable pageable) {
        Page<Award> awards = retrieveAwardPort.findByConditions(memberId, year, pageable);
        return new PagedResponseDto<>(awards.map(AwardDtoMapper::toAwardResponseDto));
    }
}
