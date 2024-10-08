package page.clab.api.global.common.slack.dto.mapper;

import page.clab.api.global.common.slack.domain.NotificationSetting;
import page.clab.api.global.common.slack.dto.response.NotificationSettingResponseDto;

public class SlackDtoMapper {

    public static NotificationSettingResponseDto toNotificationSettingResponseDto(NotificationSetting setting) {
        return NotificationSettingResponseDto.builder()
                .alertType(setting.getAlertType().getTitle())
                .enabled(setting.isEnabled())
                .build();
    }
}
