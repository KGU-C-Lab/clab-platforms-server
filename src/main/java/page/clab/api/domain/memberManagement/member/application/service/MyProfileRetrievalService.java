package page.clab.api.domain.memberManagement.member.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.member.application.dto.mapper.MemberDtoMapper;
import page.clab.api.domain.memberManagement.member.application.dto.response.MyProfileResponseDto;
import page.clab.api.domain.memberManagement.member.application.port.in.RetrieveMemberUseCase;
import page.clab.api.domain.memberManagement.member.application.port.in.RetrieveMyProfileUseCase;
import page.clab.api.domain.memberManagement.member.domain.Member;

@Service
@RequiredArgsConstructor
public class MyProfileRetrievalService implements RetrieveMyProfileUseCase {

    private final RetrieveMemberUseCase retrieveMemberUseCase;

    @Transactional(readOnly = true)
    @Override
    public MyProfileResponseDto retrieveMyProfile() {
        Member currentMember = retrieveMemberUseCase.getCurrentMember();
        return MemberDtoMapper.toMyProfileResponseDto(currentMember);
    }
}
