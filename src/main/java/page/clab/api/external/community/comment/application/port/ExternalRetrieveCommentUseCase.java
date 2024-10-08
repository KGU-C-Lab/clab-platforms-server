package page.clab.api.external.community.comment.application.port;

import page.clab.api.domain.community.comment.domain.Comment;

public interface ExternalRetrieveCommentUseCase {

    Comment getById(Long targetId);

    Long countByBoardId(Long id);
}
