package page.clab.api.domain.memberManagement.member.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.member.application.event.MemberDeletedEvent;
import page.clab.api.domain.memberManagement.member.application.port.in.RemoveMemberUseCase;
import page.clab.api.domain.memberManagement.member.application.port.out.RegisterMemberPort;
import page.clab.api.domain.memberManagement.member.application.port.out.RetrieveMemberPort;
import page.clab.api.domain.memberManagement.member.domain.Member;

@Service
@RequiredArgsConstructor
public class MemberRemoveService implements RemoveMemberUseCase {

    private final RetrieveMemberPort retrieveMemberPort;
    private final RegisterMemberPort registerMemberPort;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    @Override
    public String removeMember(String memberId) {
        Member member = retrieveMemberPort.getById(memberId);
        member.delete();
        registerMemberPort.save(member);
        eventPublisher.publishEvent(new MemberDeletedEvent(this, member.getId()));
        return member.getId();
    }
}
