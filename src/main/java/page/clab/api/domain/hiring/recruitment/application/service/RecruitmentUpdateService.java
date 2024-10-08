package page.clab.api.domain.hiring.recruitment.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.hiring.recruitment.application.dto.request.RecruitmentUpdateRequestDto;
import page.clab.api.domain.hiring.recruitment.application.port.in.RetrieveRecruitmentUseCase;
import page.clab.api.domain.hiring.recruitment.application.port.in.UpdateRecruitmentUseCase;
import page.clab.api.domain.hiring.recruitment.application.port.out.UpdateRecruitmentPort;
import page.clab.api.domain.hiring.recruitment.domain.Recruitment;

@Service
@RequiredArgsConstructor
public class RecruitmentUpdateService implements UpdateRecruitmentUseCase {

    private final RetrieveRecruitmentUseCase retrieveRecruitmentUseCase;
    private final UpdateRecruitmentPort updateRecruitmentPort;
    private final RecruitmentStatusUpdater recruitmentStatusUpdater;

    @Transactional
    @Override
    public Long updateRecruitment(Long recruitmentId, RecruitmentUpdateRequestDto requestDto) {
        Recruitment recruitment = retrieveRecruitmentUseCase.getById(recruitmentId);
        recruitment.update(requestDto);
        recruitmentStatusUpdater.updateRecruitmentStatus(recruitment);
        recruitment.validateDateRange();
        return updateRecruitmentPort.update(recruitment).getId();
    }
}
