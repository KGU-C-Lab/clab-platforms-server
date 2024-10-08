package page.clab.api.global.common.slack.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationSettingResponseDto {

    private String alertType;
    private boolean enabled;
}
