package page.clab.api.domain.community.board.application.dto.shared;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.community.board.domain.BoardCategory;

@Getter
@Builder
public class BoardCommentInfoDto {

    private final Long boardId;
    private final String memberId;
    private final String title;
    private final BoardCategory category;
}
