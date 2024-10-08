package page.clab.api.domain.memberManagement.workExperience.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.workExperience.application.dto.mapper.WorkExperienceDtoMapper;
import page.clab.api.domain.memberManagement.workExperience.application.dto.response.WorkExperienceResponseDto;
import page.clab.api.domain.memberManagement.workExperience.application.port.in.RetrieveMyWorkExperienceUseCase;
import page.clab.api.domain.memberManagement.workExperience.application.port.out.RetrieveWorkExperiencePort;
import page.clab.api.domain.memberManagement.workExperience.domain.WorkExperience;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class MyWorkExperienceRetrievalService implements RetrieveMyWorkExperienceUseCase {

    private final RetrieveWorkExperiencePort retrieveWorkExperiencePort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;

    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<WorkExperienceResponseDto> retrieveMyWorkExperience(Pageable pageable) {
        String currentMemberId = externalRetrieveMemberUseCase.getCurrentMemberId();
        Page<WorkExperience> workExperiences = retrieveWorkExperiencePort.findByMemberId(currentMemberId, pageable);
        return new PagedResponseDto<>(workExperiences.map(WorkExperienceDtoMapper::toWorkExperienceResponseDto));
    }
}
