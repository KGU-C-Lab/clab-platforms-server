package page.clab.api.domain.community.comment.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.community.comment.application.port.out.RegisterCommentPort;
import page.clab.api.domain.community.comment.application.port.out.RetrieveCommentPort;
import page.clab.api.domain.community.comment.domain.Comment;
import page.clab.api.global.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CommentPersistenceAdapter implements
        RegisterCommentPort,
        RetrieveCommentPort {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Override
    public Comment save(Comment comment) {
        CommentJpaEntity entity = commentMapper.toEntity(comment);
        CommentJpaEntity savedEntity = commentRepository.save(entity);
        return commentMapper.toDomain(savedEntity);
    }

    @Override
    public void saveAll(List<Comment> comments) {
        List<CommentJpaEntity> entities = comments.stream()
                .map(commentMapper::toEntity)
                .toList();
        commentRepository.saveAll(entities);
    }

    @Override
    public Optional<Comment> findById(Long commentId) {
        return commentRepository.findById(commentId)
                .map(commentMapper::toDomain);
    }

    @Override
    public Comment getById(Long commentId) {
        return commentRepository.findById(commentId)
                .map(commentMapper::toDomain)
                .orElseThrow(() -> new NotFoundException("[Comment] id: " + commentId + "에 해당하는 댓글이 존재하지 않습니다."));
    }

    @Override
    public Page<Comment> findAllByIsDeletedTrueAndBoardId(Long boardId, Pageable pageable) {
        return commentRepository.findAllByIsDeletedTrueAndBoardId(boardId, pageable)
                .map(commentMapper::toDomain);
    }

    @Override
    public Page<Comment> findAllByBoardIdAndParentIsNull(Long boardId, Pageable pageable) {
        return commentRepository.findAllByBoardIdAndParentIsNull(boardId, pageable)
                .map(commentMapper::toDomain);
    }

    @Override
    public Page<Comment> findAllByWriterId(String memberId, Pageable pageable) {
        return commentRepository.findAllByWriterId(memberId, pageable)
                .map(commentMapper::toDomain);
    }

    @Override
    public Long countByBoardId(Long boardId) {
        return commentRepository.countByBoardId(boardId);
    }

    @Override
    public List<Comment> findByBoardId(Long boardId) {
        return commentRepository.findByBoardId(boardId)
                .stream()
                .map(commentMapper::toDomain)
                .toList();
    }
}
