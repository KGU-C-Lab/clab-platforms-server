package page.clab.api.domain.members.blog.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BlogResponseDto {

    private Long id;
    private String title;
    private String subTitle;
    private String imageUrl;
    private String hyperlink;
    private LocalDateTime createdAt;
}
