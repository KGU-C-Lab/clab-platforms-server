package page.clab.api.domain.hiring.application.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.hiring.application.application.dto.mapper.ApplicationDtoMapper;
import page.clab.api.domain.hiring.application.application.dto.request.ApplicationRequestDto;
import page.clab.api.domain.hiring.application.application.port.in.ApplyForApplicationUseCase;
import page.clab.api.domain.hiring.application.application.port.out.RegisterApplicationPort;
import page.clab.api.domain.hiring.application.domain.Application;
import page.clab.api.external.hiring.application.application.port.ExternalRetrieveRecruitmentUseCase;
import page.clab.api.external.memberManagement.notification.application.port.ExternalSendNotificationUseCase;
import page.clab.api.global.common.slack.application.SlackService;

@Service
@RequiredArgsConstructor
public class ApplicationApplyService implements ApplyForApplicationUseCase {

    private final RegisterApplicationPort registerApplicationPort;
    private final ExternalRetrieveRecruitmentUseCase externalRetrieveRecruitmentUseCase;
    private final ExternalSendNotificationUseCase externalSendNotificationUseCase;
    private final SlackService slackService;

    @Transactional
    @Override
    public String applyForClub(ApplicationRequestDto requestDto) {
        externalRetrieveRecruitmentUseCase.validateRecruitmentForApplication(requestDto.getRecruitmentId());
        Application application = ApplicationDtoMapper.toApplication(requestDto);
        String applicationType = application.getApplicationTypeForNotificationPrefix();
        externalSendNotificationUseCase.sendNotificationToAdmins(applicationType + requestDto.getStudentId() + " " +
                requestDto.getName() + "님이 지원하였습니다.");
        slackService.sendNewApplicationNotification(requestDto);
        return registerApplicationPort.save(application).getStudentId();
    }
}
