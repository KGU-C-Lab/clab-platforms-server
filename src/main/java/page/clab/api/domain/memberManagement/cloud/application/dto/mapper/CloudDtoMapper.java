package page.clab.api.domain.memberManagement.cloud.application.dto.mapper;

import page.clab.api.domain.memberManagement.cloud.application.dto.response.CloudUsageInfo;

public class CloudDtoMapper {

    public static CloudUsageInfo toCloudUsageInfo(String memberId, Long usage) {
        return CloudUsageInfo.builder()
                .memberId(memberId)
                .usage(usage)
                .build();
    }
}
