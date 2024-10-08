package page.clab.api.domain.auth.blacklistIp.application.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.auth.blacklistIp.application.dto.mapper.BlacklistIpDtoMapper;
import page.clab.api.domain.auth.blacklistIp.application.dto.request.BlacklistIpRequestDto;
import page.clab.api.domain.auth.blacklistIp.application.port.in.RegisterBlacklistIpUseCase;
import page.clab.api.domain.auth.blacklistIp.application.port.out.RegisterBlacklistIpPort;
import page.clab.api.domain.auth.blacklistIp.application.port.out.RetrieveBlacklistIpPort;
import page.clab.api.domain.auth.blacklistIp.domain.BlacklistIp;
import page.clab.api.global.common.slack.application.SlackService;
import page.clab.api.global.common.slack.domain.SecurityAlertType;

@Service
@RequiredArgsConstructor
public class BlacklistIpRegisterService implements RegisterBlacklistIpUseCase {

    private final RegisterBlacklistIpPort registerBlacklistIpPort;
    private final RetrieveBlacklistIpPort retrieveBlacklistIpPort;
    private final SlackService slackService;

    @Transactional
    @Override
    public String registerBlacklistIp(HttpServletRequest request, BlacklistIpRequestDto requestDto) {
        String ipAddress = requestDto.getIpAddress();
        return retrieveBlacklistIpPort.findByIpAddress(ipAddress)
                .map(BlacklistIp::getIpAddress)
                .orElseGet(() -> {
                    BlacklistIp blacklistIp = BlacklistIpDtoMapper.toBlacklistIp(requestDto);
                    registerBlacklistIpPort.save(blacklistIp);
                    slackService.sendSecurityAlertNotification(request, SecurityAlertType.BLACKLISTED_IP_ADDED, "Added IP: " + ipAddress);
                    return ipAddress;
                });
    }
}
