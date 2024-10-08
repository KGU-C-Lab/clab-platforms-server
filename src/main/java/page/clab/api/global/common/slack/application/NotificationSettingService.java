package page.clab.api.global.common.slack.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.global.common.slack.dao.NotificationSettingRepository;
import page.clab.api.global.common.slack.domain.AlertType;
import page.clab.api.global.common.slack.domain.AlertTypeResolver;
import page.clab.api.global.common.slack.domain.NotificationSetting;
import page.clab.api.global.common.slack.dto.mapper.SlackDtoMapper;
import page.clab.api.global.common.slack.dto.response.NotificationSettingResponseDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationSettingService {

    private final AlertTypeResolver alertTypeResolver;
    private final NotificationSettingRepository settingRepository;

    @Transactional(readOnly = true)
    public List<NotificationSettingResponseDto> getNotificationSettings() {
        return settingRepository.findAll().stream()
                .map(SlackDtoMapper::toNotificationSettingResponseDto)
                .toList();
    }

    @Transactional
    public void updateNotificationSetting(String alertTypeName, boolean enabled) {
        AlertType alertType = alertTypeResolver.resolve(alertTypeName);
        NotificationSetting setting = getOrCreateDefaultSetting(alertType);
        setting.updateEnabled(enabled);
        settingRepository.save(setting);
    }

    @Transactional
    public NotificationSetting getOrCreateDefaultSetting(AlertType alertType) {
        return settingRepository.findByAlertType(alertType)
                .orElseGet(() -> createAndSaveDefaultSetting(alertType));
    }

    private NotificationSetting createAndSaveDefaultSetting(AlertType alertType) {
        NotificationSetting defaultSetting = NotificationSetting.createDefault(alertType);
        return settingRepository.save(defaultSetting);
    }
}
