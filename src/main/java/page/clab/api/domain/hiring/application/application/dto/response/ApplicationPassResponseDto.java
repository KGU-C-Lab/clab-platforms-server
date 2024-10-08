package page.clab.api.domain.hiring.application.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.hiring.application.domain.ApplicationType;

@Getter
@Builder
public class ApplicationPassResponseDto {

    private Long recruitmentId;
    private String name;
    private ApplicationType applicationType;
    private Boolean isPass;

    public static ApplicationPassResponseDto defaultResponse() {
        return ApplicationPassResponseDto.builder()
                .isPass(false)
                .build();
    }
}
