package page.clab.api.domain.memberManagement.workExperience.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.memberManagement.workExperience.application.port.in.RemoveWorkExperienceUseCase;
import page.clab.api.domain.memberManagement.workExperience.application.port.out.RegisterWorkExperiencePort;
import page.clab.api.domain.memberManagement.workExperience.application.port.out.RetrieveWorkExperiencePort;
import page.clab.api.domain.memberManagement.workExperience.domain.WorkExperience;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class WorkExperienceRemoveService implements RemoveWorkExperienceUseCase {

    private final RetrieveWorkExperiencePort retrieveWorkExperiencePort;
    private final RegisterWorkExperiencePort registerWorkExperiencePort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;

    @Override
    @Transactional
    public Long removeWorkExperience(Long workExperienceId) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = externalRetrieveMemberUseCase.getCurrentMemberDetailedInfo();
        WorkExperience workExperience = retrieveWorkExperiencePort.getById(workExperienceId);
        workExperience.validateAccessPermission(currentMemberInfo);
        workExperience.delete();
        return registerWorkExperiencePort.save(workExperience).getId();
    }
}
