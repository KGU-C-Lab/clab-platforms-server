package page.clab.api.domain.application.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.application.application.port.in.ToggleApplicationApprovalUseCase;
import page.clab.api.domain.application.application.port.out.RegisterApplicationPort;
import page.clab.api.domain.application.application.port.out.RetrieveApplicationPort;
import page.clab.api.domain.application.domain.Application;
import page.clab.api.domain.application.domain.ApplicationId;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationApprovalToggleService implements ToggleApplicationApprovalUseCase {

    private final RetrieveApplicationPort retrieveApplicationPort;
    private final RegisterApplicationPort registerApplicationPort;
    private final ValidationService validationService;

    @Transactional
    @Override
    public String toggleApprovalStatus(Long recruitmentId, String studentId) {
        Application application = retrieveApplicationPort.findByIdOrThrow(ApplicationId.create(studentId, recruitmentId));
        application.toggleApprovalStatus();
        validationService.checkValid(application);
        return registerApplicationPort.save(application).getStudentId();
    }
}
