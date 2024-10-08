package page.clab.api.domain.members.product.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequestDto {

    @NotNull(message = "{notNull.product.name}")
    @Schema(description = "서비스명", example = "petmily-server", required = true)
    private String name;

    @NotNull(message = "{notNull.product.description}")
    @Schema(description = "설명", example = "펫밀리 (Back) - 제10회 소프트웨어 개발보안 시큐어코딩 해커톤", required = true)
    private String description;

    @Schema(description = "URL", example = "https://github.com/KGU-C-Lab/petmily-server")
    private String url;
}
