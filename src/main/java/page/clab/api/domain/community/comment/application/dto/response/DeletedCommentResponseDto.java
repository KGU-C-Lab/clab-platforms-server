package page.clab.api.domain.community.comment.application.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class DeletedCommentResponseDto {

    private Long id;
    private String writerId;
    private String writerName;
    private String writerImageUrl;
    private Long writerRoleLevel;
    private String content;
    private Long likes;

    @JsonProperty("isOwner")
    private boolean isOwner;
    private LocalDateTime createdAt;
}
