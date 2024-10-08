package page.clab.api.domain.memberManagement.cloud.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CloudUsageInfo {

    private String memberId;
    private Long usage;
}
