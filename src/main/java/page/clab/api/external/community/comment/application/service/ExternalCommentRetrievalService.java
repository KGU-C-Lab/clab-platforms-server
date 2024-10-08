package page.clab.api.external.community.comment.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.community.comment.application.port.in.RetrieveCommentUseCase;
import page.clab.api.domain.community.comment.application.port.out.RetrieveCommentPort;
import page.clab.api.domain.community.comment.domain.Comment;
import page.clab.api.external.community.comment.application.port.ExternalRetrieveCommentUseCase;

@Service
@RequiredArgsConstructor
public class ExternalCommentRetrievalService implements ExternalRetrieveCommentUseCase {

    private final RetrieveCommentUseCase retrieveCommentUseCase;
    private final RetrieveCommentPort retrieveCommentPort;

    @Transactional(readOnly = true)
    @Override
    public Comment getById(Long targetId) {
        return retrieveCommentUseCase.getById(targetId);
    }

    @Transactional(readOnly = true)
    @Override
    public Long countByBoardId(Long id) {
        return retrieveCommentPort.countByBoardId(id);
    }
}
