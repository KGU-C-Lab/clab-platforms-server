package page.clab.api.domain.members.donation.application.dto.mapper;

import page.clab.api.domain.members.donation.application.dto.request.DonationRequestDto;
import page.clab.api.domain.members.donation.application.dto.response.DonationResponseDto;
import page.clab.api.domain.members.donation.domain.Donation;

public class DonationDtoMapper {

    public static Donation toDonation(DonationRequestDto requestDto, String memberId) {
        return Donation.builder()
                .memberId(memberId)
                .amount(requestDto.getAmount())
                .message(requestDto.getMessage())
                .isDeleted(false)
                .build();
    }

    public static DonationResponseDto toDonationResponseDto(Donation donation, String memberName) {
        return DonationResponseDto.builder()
                .id(donation.getId())
                .memberId(donation.getMemberId())
                .memberName(memberName)
                .amount(donation.getAmount())
                .message(donation.getMessage())
                .createdAt(donation.getCreatedAt())
                .build();
    }
}
