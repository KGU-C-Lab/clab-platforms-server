package page.clab.api.domain.hiring.application.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.hiring.application.adapter.out.persistence.ApplicationId;
import page.clab.api.domain.hiring.application.application.port.in.RejectApplicationUseCase;
import page.clab.api.domain.hiring.application.application.port.out.RegisterApplicationPort;
import page.clab.api.domain.hiring.application.application.port.out.RetrieveApplicationPort;
import page.clab.api.domain.hiring.application.domain.Application;

@Service
@RequiredArgsConstructor
public class RejectApplicationService implements RejectApplicationUseCase {

    private final RetrieveApplicationPort retrieveApplicationPort;
    private final RegisterApplicationPort registerApplicationPort;

    @Override
    public Long rejectApplication(Long recruitmentId, String studentId) {
        Application application = retrieveApplicationPort.getById(ApplicationId.create(studentId, recruitmentId));
        application.reject();
        registerApplicationPort.save(application);
        return recruitmentId;
    }
}
