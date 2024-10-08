package page.clab.api.domain.memberManagement.member.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.member.application.dto.mapper.MemberDtoMapper;
import page.clab.api.domain.memberManagement.member.application.dto.response.MemberRoleInfoResponseDto;
import page.clab.api.domain.memberManagement.member.application.port.in.RetrieveMemberRoleInfoUseCase;
import page.clab.api.domain.memberManagement.member.application.port.out.RetrieveMemberPort;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.domain.memberManagement.member.domain.Role;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class MemberRoleInfoRetrievalService implements RetrieveMemberRoleInfoUseCase {

    private final RetrieveMemberPort retrieveMemberPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<MemberRoleInfoResponseDto> retrieveMemberRoleInfo(String memberId, String memberName, Role role, Pageable pageable) {
        Page<Member> members = retrieveMemberPort.findMemberRoleInfoByConditions(memberId, memberName, role, pageable);
        return new PagedResponseDto<>(members.map(MemberDtoMapper::toMemberRoleInfoResponseDto));
    }
}
