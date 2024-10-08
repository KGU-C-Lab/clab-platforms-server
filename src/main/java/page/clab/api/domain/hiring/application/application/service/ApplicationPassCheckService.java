package page.clab.api.domain.hiring.application.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.hiring.application.adapter.out.persistence.ApplicationId;
import page.clab.api.domain.hiring.application.application.dto.mapper.ApplicationDtoMapper;
import page.clab.api.domain.hiring.application.application.dto.response.ApplicationPassResponseDto;
import page.clab.api.domain.hiring.application.application.port.in.CheckApplicationPassStatusUseCase;
import page.clab.api.domain.hiring.application.application.port.out.RetrieveApplicationPort;
import page.clab.api.domain.hiring.recruitment.application.port.out.RetrieveRecruitmentPort;
import page.clab.api.domain.hiring.recruitment.domain.Recruitment;

@Service
@RequiredArgsConstructor
public class ApplicationPassCheckService implements CheckApplicationPassStatusUseCase {

    private final RetrieveApplicationPort retrieveApplicationPort;
    private final RetrieveRecruitmentPort retrieveRecruitmentPort;

    @Transactional(readOnly = true)
    @Override
    public ApplicationPassResponseDto checkPassStatus(Long recruitmentId, String studentId) {
        ApplicationId id = ApplicationId.create(studentId, recruitmentId);
        Recruitment recruitment = retrieveRecruitmentPort.getById(recruitmentId);

        recruitment.validateEndDateWithin7Days();

        return retrieveApplicationPort.findById(id)
                .map(ApplicationDtoMapper::toApplicationPassResponseDto)
                .orElseGet(ApplicationPassResponseDto::defaultResponse);
    }
}
