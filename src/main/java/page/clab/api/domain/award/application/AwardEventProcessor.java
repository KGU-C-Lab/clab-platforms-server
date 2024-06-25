package page.clab.api.domain.award.application;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.award.dao.AwardRepository;
import page.clab.api.domain.award.domain.Award;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.member.event.MemberEventProcessor;
import page.clab.api.domain.member.event.MemberEventProcessorRegistry;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AwardEventProcessor implements MemberEventProcessor {

    private final AwardRepository awardRepository;

    private final MemberEventProcessorRegistry processorRegistry;

    @PostConstruct
    public void init() {
        processorRegistry.registerProcessor(this);
    }

    @Override
    @Transactional
    public void processMemberDeleted(Member member) {
        List<Award> awards = awardRepository.findByMemberId(member.getId());
        awards.forEach(Award::delete);
        awardRepository.saveAll(awards);
    }

    @Override
    public void processMemberUpdated(Member member) {
        // do nothing
    }
}
