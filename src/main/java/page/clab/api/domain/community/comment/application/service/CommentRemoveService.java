package page.clab.api.domain.community.comment.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.community.comment.application.port.in.RemoveCommentUseCase;
import page.clab.api.domain.community.comment.application.port.out.RegisterCommentPort;
import page.clab.api.domain.community.comment.application.port.out.RetrieveCommentPort;
import page.clab.api.domain.community.comment.domain.Comment;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class CommentRemoveService implements RemoveCommentUseCase {

    private final RetrieveCommentPort retrieveCommentPort;
    private final RegisterCommentPort registerCommentPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;

    @Transactional
    @Override
    public Long removeComment(Long commentId) throws PermissionDeniedException {
        Comment comment = retrieveCommentPort.getById(commentId);
        comment.validateAccessPermission(externalRetrieveMemberUseCase.getCurrentMemberDetailedInfo());
        comment.delete();
        registerCommentPort.save(comment);
        return comment.getBoardId();
    }
}
