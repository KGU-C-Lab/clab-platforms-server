package page.clab.api.domain.memberManagement.workExperience.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.workExperience.application.dto.mapper.WorkExperienceDtoMapper;
import page.clab.api.domain.memberManagement.workExperience.application.dto.request.WorkExperienceRequestDto;
import page.clab.api.domain.memberManagement.workExperience.application.port.in.RegisterWorkExperienceUseCase;
import page.clab.api.domain.memberManagement.workExperience.application.port.out.RegisterWorkExperiencePort;
import page.clab.api.domain.memberManagement.workExperience.domain.WorkExperience;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;

@Service
@RequiredArgsConstructor
public class WorkExperienceRegisterService implements RegisterWorkExperienceUseCase {

    private final RegisterWorkExperiencePort registerWorkExperiencePort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;

    @Override
    @Transactional
    public Long registerWorkExperience(WorkExperienceRequestDto requestDto) {
        String currentMemberId = externalRetrieveMemberUseCase.getCurrentMemberId();
        WorkExperience workExperience = WorkExperienceDtoMapper.toWorkExperience(requestDto, currentMemberId);
        workExperience.validateBusinessRules();
        return registerWorkExperiencePort.save(workExperience).getId();
    }
}
