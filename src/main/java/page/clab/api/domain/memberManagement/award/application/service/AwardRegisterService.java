package page.clab.api.domain.memberManagement.award.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.award.application.dto.mapper.AwardDtoMapper;
import page.clab.api.domain.memberManagement.award.application.dto.request.AwardRequestDto;
import page.clab.api.domain.memberManagement.award.application.port.in.RegisterAwardUseCase;
import page.clab.api.domain.memberManagement.award.application.port.out.RegisterAwardPort;
import page.clab.api.domain.memberManagement.award.domain.Award;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;

@Service
@RequiredArgsConstructor
public class AwardRegisterService implements RegisterAwardUseCase {

    private final RegisterAwardPort registerAwardPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;

    @Transactional
    @Override
    public Long registerAward(AwardRequestDto requestDto) {
        String currentMemberId = externalRetrieveMemberUseCase.getCurrentMemberId();
        Award award = AwardDtoMapper.toAward(requestDto, currentMemberId);
        return registerAwardPort.save(award).getId();
    }
}
