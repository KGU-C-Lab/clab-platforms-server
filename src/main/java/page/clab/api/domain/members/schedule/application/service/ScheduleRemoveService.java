package page.clab.api.domain.members.schedule.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.members.schedule.application.port.in.RemoveScheduleUseCase;
import page.clab.api.domain.members.schedule.application.port.out.RegisterSchedulePort;
import page.clab.api.domain.members.schedule.application.port.out.RetrieveSchedulePort;
import page.clab.api.domain.members.schedule.domain.Schedule;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class ScheduleRemoveService implements RemoveScheduleUseCase {

    private final RetrieveSchedulePort retrieveSchedulePort;
    private final RegisterSchedulePort registerSchedulePort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;

    @Override
    @Transactional
    public Long removeSchedule(Long scheduleId) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = externalRetrieveMemberUseCase.getCurrentMemberDetailedInfo();
        Schedule schedule = retrieveSchedulePort.getById(scheduleId);
        schedule.validateAccessPermission(currentMemberInfo);
        schedule.delete();
        return registerSchedulePort.save(schedule).getId();
    }
}
