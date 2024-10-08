package page.clab.api.domain.memberManagement.member.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.member.application.dto.mapper.MemberDtoMapper;
import page.clab.api.domain.memberManagement.member.application.dto.response.MemberBirthdayResponseDto;
import page.clab.api.domain.memberManagement.member.application.port.in.RetrieveMemberBirthdaysThisMonthUseCase;
import page.clab.api.domain.memberManagement.member.application.port.out.RetrieveMemberPort;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class MemberBirthdayRetrievalThisMonthService implements RetrieveMemberBirthdaysThisMonthUseCase {

    private final RetrieveMemberPort retrieveMemberPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<MemberBirthdayResponseDto> retrieveBirthdaysThisMonth(int month, Pageable pageable) {
        Page<Member> birthdayMembers = retrieveMemberPort.findBirthdaysThisMonth(month, pageable);
        return new PagedResponseDto<>(birthdayMembers.map(MemberDtoMapper::toMemberBirthdayResponseDto));
    }
}
