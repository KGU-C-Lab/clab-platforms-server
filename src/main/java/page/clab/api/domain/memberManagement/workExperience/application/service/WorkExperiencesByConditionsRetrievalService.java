package page.clab.api.domain.memberManagement.workExperience.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.workExperience.application.dto.mapper.WorkExperienceDtoMapper;
import page.clab.api.domain.memberManagement.workExperience.application.dto.response.WorkExperienceResponseDto;
import page.clab.api.domain.memberManagement.workExperience.application.port.in.RetrieveWorkExperiencesByConditionsUseCase;
import page.clab.api.domain.memberManagement.workExperience.application.port.out.RetrieveWorkExperiencePort;
import page.clab.api.domain.memberManagement.workExperience.domain.WorkExperience;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class WorkExperiencesByConditionsRetrievalService implements RetrieveWorkExperiencesByConditionsUseCase {

    private final RetrieveWorkExperiencePort retrieveWorkExperiencePort;

    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<WorkExperienceResponseDto> retrieveWorkExperiences(String memberId, Pageable pageable) {
        Page<WorkExperience> workExperiences = retrieveWorkExperiencePort.findByConditions(memberId, pageable);
        return new PagedResponseDto<>(workExperiences.map(WorkExperienceDtoMapper::toWorkExperienceResponseDto));
    }
}
