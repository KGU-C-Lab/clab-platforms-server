package page.clab.api.domain.community.comment.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentMyResponseDto {

    private Long id;
    private Long boardId;
    private String boardCategory;
    private String writer;
    private String writerImageUrl;
    private String content;
    private Long likes;
    private boolean hasLikeByMe;
    private LocalDateTime createdAt;
}
