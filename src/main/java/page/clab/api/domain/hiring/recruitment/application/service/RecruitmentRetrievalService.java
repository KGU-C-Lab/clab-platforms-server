package page.clab.api.domain.hiring.recruitment.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.hiring.recruitment.application.port.in.RetrieveRecruitmentUseCase;
import page.clab.api.domain.hiring.recruitment.application.port.out.RetrieveRecruitmentPort;
import page.clab.api.domain.hiring.recruitment.domain.Recruitment;

@Service
@RequiredArgsConstructor
public class RecruitmentRetrievalService implements RetrieveRecruitmentUseCase {

    private final RetrieveRecruitmentPort retrieveRecruitmentPort;

    @Override
    public Recruitment getById(Long recruitmentId) {
        return retrieveRecruitmentPort.getById(recruitmentId);
    }
}
