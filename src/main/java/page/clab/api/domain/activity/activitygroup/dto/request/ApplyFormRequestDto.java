package page.clab.api.domain.activity.activitygroup.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplyFormRequestDto {

    @NotNull(message = "{notnull.applyForm.applyReason}")
    @Schema(description = "지원 동기", example = "백엔드에 관심이 있어서")
    private String applyReason;
}
