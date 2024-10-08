package page.clab.api.domain.memberManagement.member.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.member.application.dto.mapper.MemberDtoMapper;
import page.clab.api.domain.memberManagement.member.application.dto.response.MemberResponseDto;
import page.clab.api.domain.memberManagement.member.application.port.in.RetrieveMembersByConditionsUseCase;
import page.clab.api.domain.memberManagement.member.application.port.out.RetrieveMemberPort;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class MembersByConditionsRetrievalService implements RetrieveMembersByConditionsUseCase {

    private final RetrieveMemberPort retrieveMemberPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<MemberResponseDto> retrieveMembers(String id, String name, Pageable pageable) {
        Page<Member> members = retrieveMemberPort.findByConditions(id, name, pageable);
        return new PagedResponseDto<>(members.map(MemberDtoMapper::toMemberResponseDto));
    }
}
