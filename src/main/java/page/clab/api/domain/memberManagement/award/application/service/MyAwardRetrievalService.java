package page.clab.api.domain.memberManagement.award.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.award.application.dto.mapper.AwardDtoMapper;
import page.clab.api.domain.memberManagement.award.application.dto.response.AwardResponseDto;
import page.clab.api.domain.memberManagement.award.application.port.in.RetrieveMyAwardsUseCase;
import page.clab.api.domain.memberManagement.award.application.port.out.RetrieveAwardPort;
import page.clab.api.domain.memberManagement.award.domain.Award;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class MyAwardRetrievalService implements RetrieveMyAwardsUseCase {

    private final RetrieveAwardPort retrieveAwardPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<AwardResponseDto> retrieveMyAwards(Pageable pageable) {
        String currentMemberId = externalRetrieveMemberUseCase.getCurrentMemberId();
        Page<Award> awards = retrieveAwardPort.findByMemberId(currentMemberId, pageable);
        return new PagedResponseDto<>(awards.map(AwardDtoMapper::toAwardResponseDto));
    }
}
