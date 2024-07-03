package page.clab.api.domain.award.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.award.application.port.in.RetrieveMyAwardsUseCase;
import page.clab.api.domain.award.application.port.out.RetrieveMyAwardsPort;
import page.clab.api.domain.award.domain.Award;
import page.clab.api.domain.award.dto.response.AwardResponseDto;
import page.clab.api.domain.member.application.port.in.RetrieveMemberUseCase;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class MyAwardRetrievalService implements RetrieveMyAwardsUseCase {

    private final RetrieveMyAwardsPort retrieveMyAwardsPort;
    private final RetrieveMemberUseCase retrieveMemberUseCase;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<AwardResponseDto> retrieve(Pageable pageable) {
        String currentMemberId = retrieveMemberUseCase.getCurrentMemberId();
        Page<Award> awards = retrieveMyAwardsPort.findByMemberId(currentMemberId, pageable);
        return new PagedResponseDto<>(awards.map(AwardResponseDto::toDto));
    }
}
