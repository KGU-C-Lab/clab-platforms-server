package page.clab.api.domain.members.blog.application.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BlogDetailsResponseDto {

    private Long id;
    private String memberId;
    private String name;
    private String title;
    private String subTitle;
    private String content;
    private String imageUrl;
    private String hyperlink;

    @JsonProperty("isOwner")
    private Boolean isOwner;
    private LocalDateTime createdAt;
}
