package page.clab.api.domain.memberManagement.award.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.award.application.port.in.RemoveAwardUseCase;
import page.clab.api.domain.memberManagement.award.application.port.out.RegisterAwardPort;
import page.clab.api.domain.memberManagement.award.application.port.out.RetrieveAwardPort;
import page.clab.api.domain.memberManagement.award.domain.Award;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class AwardRemoveService implements RemoveAwardUseCase {

    private final RetrieveAwardPort retrieveAwardPort;
    private final RegisterAwardPort registerAwardPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;

    @Transactional
    @Override
    public Long removeAward(Long awardId) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = externalRetrieveMemberUseCase.getCurrentMemberDetailedInfo();
        Award award = retrieveAwardPort.getById(awardId);
        award.validateAccessPermission(currentMemberInfo);
        award.delete();
        return registerAwardPort.save(award).getId();
    }
}
