package page.clab.api.domain.members.blog.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlogRequestDto {

    @NotNull(message = "{notNull.blog.title}")
    @Schema(description = "제목", example = "Swagger Docs의 접근 권한을 제어하기 위한 여정", required = true)
    private String title;

    @NotNull(message = "{notNull.blog.subTitle}")
    @Schema(description = "부제목", example = "Basic Auth와 JWT를 같이 사용하기 위한 Spring Security 디버깅", required = true)
    private String subTitle;

    @NotNull(message = "{notNull.blog.content}")
    @Schema(description = "내용", example = "NestJs는 스웨거 설정에 있던데 스프링은........", required = true)
    private String content;

    @Schema(description = "이미지 URL", example = "https://www.clab.page/assets/logoWhite-fc1ef9a0.webp")
    private String imageUrl;

    @Schema(description = "하이퍼링크", example = "https://www.clab.page")
    private String hyperlink;
}
