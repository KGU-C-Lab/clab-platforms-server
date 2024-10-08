package page.clab.api.domain.community.comment.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.community.comment.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface RetrieveCommentPort {

    Optional<Comment> findById(Long commentId);

    Comment getById(Long commentId);

    Page<Comment> findAllByIsDeletedTrueAndBoardId(Long boardId, Pageable pageable);

    Page<Comment> findAllByBoardIdAndParentIsNull(Long boardId, Pageable pageable);

    Page<Comment> findAllByWriterId(String memberId, Pageable pageable);

    List<Comment> findByBoardId(Long boardId);

    Long countByBoardId(Long boardId);
}
