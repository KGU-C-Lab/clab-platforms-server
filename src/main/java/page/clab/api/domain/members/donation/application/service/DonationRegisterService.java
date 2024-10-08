package page.clab.api.domain.members.donation.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.members.donation.application.dto.mapper.DonationDtoMapper;
import page.clab.api.domain.members.donation.application.dto.request.DonationRequestDto;
import page.clab.api.domain.members.donation.application.port.in.RegisterDonationUseCase;
import page.clab.api.domain.members.donation.application.port.out.RegisterDonationPort;
import page.clab.api.domain.members.donation.domain.Donation;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;

@Service
@RequiredArgsConstructor
public class DonationRegisterService implements RegisterDonationUseCase {

    private final RegisterDonationPort registerDonationPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;

    @Transactional
    @Override
    public Long registerDonation(DonationRequestDto requestDto) {
        String currentMemberId = externalRetrieveMemberUseCase.getCurrentMemberId();
        Donation donation = DonationDtoMapper.toDonation(requestDto, currentMemberId);
        return registerDonationPort.save(donation).getId();
    }
}
