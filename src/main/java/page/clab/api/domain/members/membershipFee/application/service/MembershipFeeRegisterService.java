package page.clab.api.domain.members.membershipFee.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBasicInfoDto;
import page.clab.api.domain.members.membershipFee.application.dto.mapper.MembershipFeeDtoMapper;
import page.clab.api.domain.members.membershipFee.application.dto.request.MembershipFeeRequestDto;
import page.clab.api.domain.members.membershipFee.application.port.in.RegisterMembershipFeeUseCase;
import page.clab.api.domain.members.membershipFee.application.port.out.RegisterMembershipFeePort;
import page.clab.api.domain.members.membershipFee.domain.MembershipFee;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.external.memberManagement.notification.application.port.ExternalSendNotificationUseCase;
import page.clab.api.global.common.slack.application.SlackService;
import page.clab.api.global.common.slack.domain.SlackMembershipFeeInfo;

@Service
@RequiredArgsConstructor
public class MembershipFeeRegisterService implements RegisterMembershipFeeUseCase {

    private final RegisterMembershipFeePort registerMembershipFeePort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final ExternalSendNotificationUseCase externalSendNotificationUseCase;
    private final SlackService slackService;

    @Transactional
    @Override
    public Long registerMembershipFee(MembershipFeeRequestDto requestDto) {
        MemberBasicInfoDto memberInfo = externalRetrieveMemberUseCase.getCurrentMemberBasicInfo();
        MembershipFee membershipFee = MembershipFeeDtoMapper.toMembershipFee(requestDto, memberInfo.getMemberId());
        externalSendNotificationUseCase.sendNotificationToAdmins("새로운 회비 내역이 등록되었습니다.");
        SlackMembershipFeeInfo membershipFeeInfo = SlackMembershipFeeInfo.create(membershipFee, memberInfo);
        slackService.sendNewMembershipFeeNotification(membershipFeeInfo);
        return registerMembershipFeePort.save(membershipFee).getId();
    }
}
