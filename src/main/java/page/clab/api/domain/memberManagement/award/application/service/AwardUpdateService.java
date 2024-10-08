package page.clab.api.domain.memberManagement.award.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.award.application.dto.request.AwardUpdateRequestDto;
import page.clab.api.domain.memberManagement.award.application.port.in.UpdateAwardUseCase;
import page.clab.api.domain.memberManagement.award.application.port.out.RegisterAwardPort;
import page.clab.api.domain.memberManagement.award.application.port.out.RetrieveAwardPort;
import page.clab.api.domain.memberManagement.award.domain.Award;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class AwardUpdateService implements UpdateAwardUseCase {

    private final RetrieveAwardPort retrieveAwardPort;
    private final RegisterAwardPort registerAwardPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;

    @Transactional
    @Override
    public Long updateAward(Long awardId, AwardUpdateRequestDto requestDto) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = externalRetrieveMemberUseCase.getCurrentMemberDetailedInfo();
        Award award = retrieveAwardPort.getById(awardId);
        award.validateAccessPermission(currentMemberInfo);
        award.update(requestDto);
        return registerAwardPort.save(award).getId();
    }
}
