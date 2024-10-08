package page.clab.api.domain.community.accuse.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.community.accuse.adapter.out.persistence.AccuseTargetId;
import page.clab.api.domain.community.accuse.application.port.in.ChangeAccusationStatusUseCase;
import page.clab.api.domain.community.accuse.application.port.out.RegisterAccuseTargetPort;
import page.clab.api.domain.community.accuse.application.port.out.RetrieveAccusePort;
import page.clab.api.domain.community.accuse.application.port.out.RetrieveAccuseTargetPort;
import page.clab.api.domain.community.accuse.domain.Accuse;
import page.clab.api.domain.community.accuse.domain.AccuseStatus;
import page.clab.api.domain.community.accuse.domain.AccuseTarget;
import page.clab.api.domain.community.accuse.domain.TargetType;
import page.clab.api.external.memberManagement.notification.application.port.ExternalSendNotificationUseCase;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccusationStatusService implements ChangeAccusationStatusUseCase {

    private final RetrieveAccusePort retrieveAccusePort;
    private final RetrieveAccuseTargetPort retrieveAccuseTargetPort;
    private final RegisterAccuseTargetPort registerAccuseTargetPort;
    private final ExternalSendNotificationUseCase externalSendNotificationUseCase;

    @Transactional
    @Override
    public Long changeAccusationStatus(TargetType type, Long targetId, AccuseStatus status) {
        AccuseTarget target = retrieveAccuseTargetPort.getById(AccuseTargetId.create(type, targetId));
        target.updateStatus(status);
        sendStatusUpdateNotifications(status, target);
        return registerAccuseTargetPort.save(target).getTargetReferenceId();
    }

    private void sendStatusUpdateNotifications(AccuseStatus status, AccuseTarget target) {
        List<String> memberIds = retrieveAccusePort.findByTarget(target.getTargetType(), target.getTargetReferenceId()).stream()
                .map(Accuse::getMemberId)
                .collect(Collectors.toList());
        externalSendNotificationUseCase.sendNotificationToMembers(memberIds, "신고 상태가 " + status.getDescription() + "(으)로 변경되었습니다.");
    }
}
