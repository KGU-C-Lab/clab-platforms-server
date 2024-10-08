package page.clab.api.domain.community.comment.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.community.board.application.dto.shared.BoardCommentInfoDto;
import page.clab.api.domain.community.board.domain.Board;
import page.clab.api.domain.community.comment.application.dto.mapper.CommentDtoMapper;
import page.clab.api.domain.community.comment.application.dto.request.CommentRequestDto;
import page.clab.api.domain.community.comment.application.port.in.RegisterCommentUseCase;
import page.clab.api.domain.community.comment.application.port.out.RegisterCommentPort;
import page.clab.api.domain.community.comment.application.port.out.RetrieveCommentPort;
import page.clab.api.domain.community.comment.domain.Comment;
import page.clab.api.external.community.board.application.port.ExternalRetrieveBoardUseCase;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.external.memberManagement.notification.application.port.ExternalSendNotificationUseCase;

@Service
@RequiredArgsConstructor
public class CommentRegisterService implements RegisterCommentUseCase {

    private final RegisterCommentPort registerCommentPort;
    private final RetrieveCommentPort retrieveCommentPort;
    private final ExternalRetrieveBoardUseCase externalRetrieveBoardUseCase;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final ExternalSendNotificationUseCase externalSendNotificationUseCase;

    @Transactional
    @Override
    public Long registerComment(Long parentId, Long boardId, CommentRequestDto requestDto) {
        Comment comment = createAndStoreComment(parentId, boardId, requestDto);
        sendNotificationForNewComment(comment);
        return boardId;
    }

    private Comment createAndStoreComment(Long parentId, Long boardId, CommentRequestDto requestDto) {
        String currentMemberId = externalRetrieveMemberUseCase.getCurrentMemberId();
        Board board = externalRetrieveBoardUseCase.getById(boardId);
        Comment parent = findParentComment(parentId);
        Comment comment = CommentDtoMapper.toComment(requestDto, board.getId(), currentMemberId, parent);
        if (parent != null) {
            parent.addChildComment(comment);
        }
        return registerCommentPort.save(comment);
    }

    private Comment findParentComment(Long parentId) {
        return parentId != null ? retrieveCommentPort.findById(parentId).orElse(null) : null;
    }

    private void sendNotificationForNewComment(Comment comment) {
        BoardCommentInfoDto boardInfo = externalRetrieveBoardUseCase.getBoardCommentInfoById(comment.getBoardId());
        String notificationMessage = String.format("[%s] 새로운 댓글이 등록되었습니다.", boardInfo.getTitle());
        externalSendNotificationUseCase.sendNotificationToMember(boardInfo.getMemberId(), notificationMessage);
    }
}
