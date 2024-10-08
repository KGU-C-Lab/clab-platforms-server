package page.clab.api.external.hiring.application.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.hiring.recruitment.application.port.out.RetrieveRecruitmentPort;
import page.clab.api.domain.hiring.recruitment.domain.Recruitment;
import page.clab.api.external.hiring.application.application.port.ExternalRetrieveRecruitmentUseCase;

@Service
@RequiredArgsConstructor
public class ExternalRecruitmentRetrievalService implements ExternalRetrieveRecruitmentUseCase {

    private final RetrieveRecruitmentPort retrieveRecruitmentPort;

    @Transactional(readOnly = true)
    @Override
    public void validateRecruitmentForApplication(Long recruitmentId) {
        Recruitment recruitment = retrieveRecruitmentPort.getById(recruitmentId);
        recruitment.validateRecruiting();
    }
}
