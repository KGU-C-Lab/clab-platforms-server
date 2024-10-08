package page.clab.api.domain.community.comment.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import page.clab.api.domain.community.comment.application.port.out.RegisterCommentLikePort;
import page.clab.api.domain.community.comment.application.port.out.RemoveCommentLikePort;
import page.clab.api.domain.community.comment.application.port.out.RetrieveCommentLikePort;
import page.clab.api.domain.community.comment.domain.CommentLike;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CommentLikePersistenceAdapter implements
        RegisterCommentLikePort,
        RemoveCommentLikePort,
        RetrieveCommentLikePort {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentLikeMapper commentLikeMapper;

    @Override
    public CommentLike save(CommentLike commentLike) {
        CommentLikeJpaEntity entity = commentLikeMapper.toEntity(commentLike);
        CommentLikeJpaEntity savedEntity = commentLikeRepository.save(entity);
        return commentLikeMapper.toDomain(savedEntity);
    }

    @Override
    public void delete(CommentLike commentLike) {
        commentLikeRepository.deleteById(commentLike.getCommentLikeId());
    }

    @Override
    public Optional<CommentLike> findByCommentIdAndMemberId(Long commentId, String memberId) {
        return commentLikeRepository.findByCommentIdAndMemberId(commentId, memberId)
                .map(commentLikeMapper::toDomain);
    }
}
