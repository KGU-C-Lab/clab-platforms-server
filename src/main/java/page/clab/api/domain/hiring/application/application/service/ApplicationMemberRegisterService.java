package page.clab.api.domain.hiring.application.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.hiring.application.application.dto.mapper.ApplicationDtoMapper;
import page.clab.api.domain.hiring.application.application.dto.request.ApplicationMemberCreationDto;
import page.clab.api.domain.hiring.application.application.event.ApplicationMemberCreatedEvent;
import page.clab.api.domain.hiring.application.application.event.PositionCreatedByApplicationEvent;
import page.clab.api.domain.hiring.application.application.exception.NotApprovedApplicationException;
import page.clab.api.domain.hiring.application.application.port.in.RegisterMembersByRecruitmentUseCase;
import page.clab.api.domain.hiring.application.application.port.out.RetrieveApplicationPort;
import page.clab.api.domain.hiring.application.domain.Application;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.domain.memberManagement.position.domain.PositionType;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.external.memberManagement.position.application.port.ExternalRetrievePositionUseCase;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationMemberRegisterService implements RegisterMembersByRecruitmentUseCase {

    private final RetrieveApplicationPort retrieveApplicationPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final ExternalRetrievePositionUseCase externalRetrievePositionUseCase;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    @Override
    public List<String> registerMembersByRecruitment(Long recruitmentId) {
        List<Application> applications = retrieveApplicationPort.findByRecruitmentIdAndIsPass(recruitmentId, true);
        return applications.stream()
                .map(this::createMemberFromApplication)
                .toList();
    }

    @Transactional
    @Override
    public String registerMembersByRecruitment(Long recruitmentId, String studentId) {
        Application application = retrieveApplicationPort.getByRecruitmentIdAndStudentId(recruitmentId, studentId);
        validateApplicationIsPass(application);
        return createMemberFromApplication(application);
    }

    private void validateApplicationIsPass(Application application) {
        if(!application.getIsPass()) {
            throw new NotApprovedApplicationException("승인되지 않은 지원서입니다.");
        }
    }

    private String createMemberFromApplication(Application application) {
        Member member = createMemberByApplication(application);
        createPositionByMember(member);
        return member.getId();
    }

    private Member createMemberByApplication(Application application) {
        return externalRetrieveMemberUseCase.findById(application.getStudentId())
                .orElseGet(() -> {
                    ApplicationMemberCreationDto dto = ApplicationDtoMapper.toApplicationMemberCreationDto(application);
                    eventPublisher.publishEvent(new ApplicationMemberCreatedEvent(this, dto));
                    return externalRetrieveMemberUseCase.getById(application.getStudentId());
                });
    }

    public void createPositionByMember(Member member) {
        if(isMemberPositionRegistered(member)) {
            log.warn("이미 직책이 있는 회원입니다: {}", member.getId());
            return;
        }
        eventPublisher.publishEvent(new PositionCreatedByApplicationEvent(this, member.getId()));
    }

    private boolean isMemberPositionRegistered(Member member) {
        return externalRetrievePositionUseCase
                .findByMemberIdAndYearAndPositionType(member.getId(), String.valueOf(LocalDate.now().getYear()), PositionType.MEMBER)
                .isPresent();
    }
}
