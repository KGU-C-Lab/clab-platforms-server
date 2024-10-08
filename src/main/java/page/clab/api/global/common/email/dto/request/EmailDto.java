package page.clab.api.global.common.email.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.global.common.email.domain.EmailTemplateType;

import java.util.List;

@Getter
@Setter
@Builder
public class EmailDto {

    @NotNull(message = "{notNull.email.to}")
    @Schema(description = "받는 사람 이메일 리스트", example = "[\"" + "clab.coreteam@gmail.com" + "\", \"" + "clab.coreteam@gmail.com" + "\"]")
    private List<String> to;

    @NotNull(message = "{notNull.email.subject}")
    @Schema(description = "제목", example = "제목")
    private String subject;

    @NotNull(message = "{notNull.email.content}")
    @Schema(description = "내용", example = "내용")
    private String content;

    @NotNull(message = "{notNull.email.templateType}")
    @Schema(description = "이메일 템플릿", example = "ACCOUNT_CREATION")
    private EmailTemplateType emailTemplateType;
}
