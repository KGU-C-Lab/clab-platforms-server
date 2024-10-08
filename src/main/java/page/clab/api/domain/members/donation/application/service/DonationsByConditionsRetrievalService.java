package page.clab.api.domain.members.donation.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBasicInfoDto;
import page.clab.api.domain.members.donation.application.dto.mapper.DonationDtoMapper;
import page.clab.api.domain.members.donation.application.dto.response.DonationResponseDto;
import page.clab.api.domain.members.donation.application.port.in.RetrieveDonationsByConditionsUseCase;
import page.clab.api.domain.members.donation.application.port.out.RetrieveDonationPort;
import page.clab.api.domain.members.donation.domain.Donation;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.common.dto.PagedResponseDto;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DonationsByConditionsRetrievalService implements RetrieveDonationsByConditionsUseCase {

    private final RetrieveDonationPort retrieveDonationPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<DonationResponseDto> retrieveDonations(String memberId, String name, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Page<Donation> donations = retrieveDonationPort.findByConditions(memberId, name, startDate, endDate, pageable);
        return new PagedResponseDto<>(donations.map(donation -> {
            MemberBasicInfoDto memberInfo = externalRetrieveMemberUseCase.getMemberBasicInfoById(donation.getMemberId());
            return DonationDtoMapper.toDonationResponseDto(donation, memberInfo.getMemberName());
        }));
    }
}
