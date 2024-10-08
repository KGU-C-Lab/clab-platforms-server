package page.clab.api.external.memberManagement.member.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.member.application.port.out.RegisterMemberPort;
import page.clab.api.domain.memberManagement.member.application.port.out.RetrieveMemberPort;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.external.memberManagement.member.application.port.ExternalUpdateMemberUseCase;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ExternalMemberUpdateService implements ExternalUpdateMemberUseCase {

    private final RetrieveMemberPort retrieveMemberPort;
    private final RegisterMemberPort registerMemberPort;

    @Transactional
    @Override
    public void updateLastLoginTime(String memberId) {
        Member member = retrieveMemberPort.getById(memberId);
        member.updateLastLoginTime();
        registerMemberPort.save(member);
    }

    @Transactional
    @Override
    public void updateLoanSuspensionDate(String memberId, LocalDateTime loanSuspensionDate) {
        Member member = retrieveMemberPort.getById(memberId);
        member.updateLoanSuspensionDate(loanSuspensionDate);
        registerMemberPort.save(member);
    }
}
