package page.clab.api.global.common.file.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DeleteFileRequestDto {

    @NotNull(message = "{notNull.file.url}")
    @Schema(description = "파일경로", example = "/resources/files/forms/123456.png", required = true)
    private String url;
}
