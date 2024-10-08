package page.clab.api.domain.community.board.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BoardListResponseDto {

    private Long id;
    private String writerId;
    private String writerName;
    private String category;
    private String title;
    private String content;
    private Long commentCount;
    private String imageUrl;
    private LocalDateTime createdAt;
}
