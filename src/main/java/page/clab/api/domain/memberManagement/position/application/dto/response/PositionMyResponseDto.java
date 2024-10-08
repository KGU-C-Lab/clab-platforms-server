package page.clab.api.domain.memberManagement.position.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.memberManagement.position.domain.PositionType;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class PositionMyResponseDto {

    private Long id;
    private String name;
    private String email;
    private String imageUrl;
    private String interests;
    private String githubUrl;
    private Map<String, List<PositionType>> positionTypes;
}
