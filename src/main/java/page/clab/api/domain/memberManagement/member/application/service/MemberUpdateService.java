package page.clab.api.domain.memberManagement.member.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.member.application.dto.request.MemberUpdateRequestDto;
import page.clab.api.domain.memberManagement.member.application.event.MemberUpdatedEvent;
import page.clab.api.domain.memberManagement.member.application.port.in.RetrieveMemberUseCase;
import page.clab.api.domain.memberManagement.member.application.port.in.UpdateMemberUseCase;
import page.clab.api.domain.memberManagement.member.application.port.out.RetrieveMemberPort;
import page.clab.api.domain.memberManagement.member.application.port.out.UpdateMemberPort;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.global.common.file.application.FileService;
import page.clab.api.global.common.file.dto.mapper.FileDtoMapper;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class MemberUpdateService implements UpdateMemberUseCase {

    private final RetrieveMemberUseCase retrieveMemberUseCase;
    private final RetrieveMemberPort retrieveMemberPort;
    private final UpdateMemberPort updateMemberPort;
    private final FileService fileService;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    @Override
    public String updateMember(String memberId, MemberUpdateRequestDto requestDto) throws PermissionDeniedException {
        Member currentMember = retrieveMemberUseCase.getCurrentMember();
        Member member = retrieveMemberPort.getById(memberId);
        member.validateAccessPermission(currentMember);
        updateMember(requestDto, member);
        updateMemberPort.update(member);
        eventPublisher.publishEvent(new MemberUpdatedEvent(this, member.getId()));
        return member.getId();
    }

    private void updateMember(MemberUpdateRequestDto requestDto, Member member) throws PermissionDeniedException {
        String previousImageUrl = member.getImageUrl();
        member.update(requestDto, passwordEncoder);
        if (requestDto.getImageUrl() != null && requestDto.getImageUrl().isEmpty()) {
            member.clearImageUrl();
            fileService.deleteFile(FileDtoMapper.toDeletedFileRequestDto(previousImageUrl));
        }
    }
}
