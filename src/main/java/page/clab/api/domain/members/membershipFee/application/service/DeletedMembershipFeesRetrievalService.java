package page.clab.api.domain.members.membershipFee.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.members.membershipFee.application.dto.mapper.MembershipFeeDtoMapper;
import page.clab.api.domain.members.membershipFee.application.dto.response.MembershipFeeResponseDto;
import page.clab.api.domain.members.membershipFee.application.port.in.RetrieveDeletedMembershipFeesUseCase;
import page.clab.api.domain.members.membershipFee.application.port.out.RetrieveMembershipFeePort;
import page.clab.api.domain.members.membershipFee.domain.MembershipFee;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class DeletedMembershipFeesRetrievalService implements RetrieveDeletedMembershipFeesUseCase {

    private final RetrieveMembershipFeePort retrieveMembershipFeePort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<MembershipFeeResponseDto> retrieveDeletedMembershipFees(Pageable pageable) {
        MemberDetailedInfoDto memberInfo = externalRetrieveMemberUseCase.getCurrentMemberDetailedInfo();
        Page<MembershipFee> membershipFees = retrieveMembershipFeePort.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(membershipFees.map(membershipFee ->
                MembershipFeeDtoMapper.toMembershipFeeResponseDto(membershipFee, memberInfo.getMemberName(), memberInfo.isAdminRole())));
    }
}
