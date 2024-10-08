package page.clab.api.domain.community.comment.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.community.comment.application.dto.response.CommentResponseDto;
import page.clab.api.domain.community.comment.domain.Comment;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveCommentUseCase {

    PagedResponseDto<CommentResponseDto> retrieveComments(Long boardId, Pageable pageable);

    Comment getById(Long commentId);
}
