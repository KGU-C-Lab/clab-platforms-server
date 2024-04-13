package page.clab.api.domain.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentUpdateRequestDto {

    @NotNull(message = "{notNull.comment.content}")
    @Schema(description = "내용", example = "댓글 내용")
    private String content;

    @NotNull(message = "{notNull.comment.wantAnonymous}")
    @Schema(description = "익명 사용 여부", example = "false")
    private boolean wantAnonymous;

}
