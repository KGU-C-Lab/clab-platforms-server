package page.clab.api.domain.memberManagement.position.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.memberManagement.position.domain.PositionType;

@Getter
@Builder
public class PositionResponseDto {

    private Long id;
    private String name;
    private String email;
    private String imageUrl;
    private String interests;
    private String githubUrl;
    private PositionType positionType;
    private String year;
}
