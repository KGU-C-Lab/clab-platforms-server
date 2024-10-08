package page.clab.api.domain.community.board.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.community.board.application.dto.request.BoardUpdateRequestDto;
import page.clab.api.domain.community.board.application.event.BoardUpdatedEvent;
import page.clab.api.domain.community.board.application.port.in.UpdateBoardUseCase;
import page.clab.api.domain.community.board.application.port.out.RegisterBoardPort;
import page.clab.api.domain.community.board.application.port.out.RetrieveBoardPort;
import page.clab.api.domain.community.board.domain.Board;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class BoardUpdateService implements UpdateBoardUseCase {

    private final RetrieveBoardPort retrieveBoardPort;
    private final RegisterBoardPort registerBoardPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    @Override
    public String updateBoard(Long boardId, BoardUpdateRequestDto requestDto) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = externalRetrieveMemberUseCase.getCurrentMemberDetailedInfo();
        Board board = retrieveBoardPort.getById(boardId);
        board.validateAccessPermission(currentMemberInfo);
        board.update(requestDto);
        registerBoardPort.save(board);
        eventPublisher.publishEvent(new BoardUpdatedEvent(this, board.getId()));
        return board.getCategory().getKey();
    }
}
