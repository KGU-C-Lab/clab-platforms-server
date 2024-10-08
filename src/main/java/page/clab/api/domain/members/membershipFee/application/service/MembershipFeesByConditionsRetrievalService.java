package page.clab.api.domain.members.membershipFee.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBasicInfoDto;
import page.clab.api.domain.members.membershipFee.application.dto.mapper.MembershipFeeDtoMapper;
import page.clab.api.domain.members.membershipFee.application.dto.response.MembershipFeeResponseDto;
import page.clab.api.domain.members.membershipFee.application.port.in.RetrieveMembershipFeesByConditionsUseCase;
import page.clab.api.domain.members.membershipFee.application.port.out.RetrieveMembershipFeePort;
import page.clab.api.domain.members.membershipFee.domain.MembershipFee;
import page.clab.api.domain.members.membershipFee.domain.MembershipFeeStatus;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class MembershipFeesByConditionsRetrievalService implements RetrieveMembershipFeesByConditionsUseCase {

    private final RetrieveMembershipFeePort retrieveMembershipFeePort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<MembershipFeeResponseDto> retrieveMembershipFees(String memberId, String memberName, String category, MembershipFeeStatus status, Pageable pageable) {
        Page<MembershipFee> membershipFees = retrieveMembershipFeePort.findByConditions(memberId, memberName, category, status, pageable);
        boolean currentMemberIsAdmin = externalRetrieveMemberUseCase.getCurrentMemberDetailedInfo().isAdminRole();
        return new PagedResponseDto<>(membershipFees.map(membership -> getMembershipFeeResponseDto(membership, currentMemberIsAdmin)));
    }

    private MembershipFeeResponseDto getMembershipFeeResponseDto(MembershipFee membershipFee, boolean isAdminRole) {
        MemberBasicInfoDto memberInfo = externalRetrieveMemberUseCase.getMemberBasicInfoById(membershipFee.getMemberId());
        return MembershipFeeDtoMapper.toMembershipFeeResponseDto(membershipFee, memberInfo.getMemberName(), isAdminRole);
    }
}
