package page.clab.api.domain.community.comment.application.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class CommentResponseDto {

    private Long id;
    private Boolean isDeleted;
    private String writerId;
    private String writerName;
    private String writerImageUrl;
    private Long writerRoleLevel;
    private String content;
    private List<CommentResponseDto> children;
    private Long likes;
    private Boolean hasLikeByMe;

    @JsonProperty("isOwner")
    private Boolean isOwner;
    private LocalDateTime createdAt;
}
