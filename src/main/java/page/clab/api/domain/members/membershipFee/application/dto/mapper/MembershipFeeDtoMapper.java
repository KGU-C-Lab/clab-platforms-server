package page.clab.api.domain.members.membershipFee.application.dto.mapper;

import page.clab.api.domain.members.membershipFee.application.dto.request.MembershipFeeRequestDto;
import page.clab.api.domain.members.membershipFee.application.dto.response.MembershipFeeResponseDto;
import page.clab.api.domain.members.membershipFee.domain.MembershipFee;
import page.clab.api.domain.members.membershipFee.domain.MembershipFeeStatus;

public class MembershipFeeDtoMapper {

    public static MembershipFee toMembershipFee(MembershipFeeRequestDto requestDto, String memberId) {
        return MembershipFee.builder()
                .memberId(memberId)
                .category(requestDto.getCategory())
                .account(requestDto.getAccount())
                .amount(requestDto.getAmount())
                .content(requestDto.getContent())
                .imageUrl(requestDto.getImageUrl())
                .status(MembershipFeeStatus.PENDING)
                .isDeleted(false)
                .build();
    }

    public static MembershipFeeResponseDto toMembershipFeeResponseDto(MembershipFee membershipFee, String memberName, boolean isAdminRole) {
        return MembershipFeeResponseDto.builder()
                .id(membershipFee.getId())
                .memberId(membershipFee.getMemberId())
                .memberName(memberName)
                .category(membershipFee.getCategory())
                .account(isAdminRole ? membershipFee.getAccount() : null)
                .amount(membershipFee.getAmount())
                .content(membershipFee.getContent())
                .imageUrl(membershipFee.getImageUrl())
                .status(membershipFee.getStatus())
                .createdAt(membershipFee.getCreatedAt())
                .build();
    }
}
