package page.clab.api.domain.community.board.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BoardMyResponseDto {

    private Long id;
    private String category;
    private String writerName;
    private String title;
    private String imageUrl;
    private LocalDateTime createdAt;
}
