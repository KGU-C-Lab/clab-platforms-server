package page.clab.api.domain.hiring.recruitment.application.port.in;


import page.clab.api.domain.hiring.recruitment.domain.Recruitment;

public interface RetrieveRecruitmentUseCase {
    Recruitment getById(Long recruitmentId);
}
