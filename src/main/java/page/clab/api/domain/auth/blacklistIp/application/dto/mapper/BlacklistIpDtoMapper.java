package page.clab.api.domain.auth.blacklistIp.application.dto.mapper;

import page.clab.api.domain.auth.blacklistIp.application.dto.request.BlacklistIpRequestDto;
import page.clab.api.domain.auth.blacklistIp.application.dto.response.BlacklistIpResponseDto;
import page.clab.api.domain.auth.blacklistIp.domain.BlacklistIp;

public class BlacklistIpDtoMapper {

    public static BlacklistIp toBlacklistIp(BlacklistIpRequestDto requestDto) {
        return BlacklistIp.builder()
                .ipAddress(requestDto.getIpAddress())
                .reason(requestDto.getReason())
                .build();
    }

    public static BlacklistIpResponseDto toBlacklistIpResponseDto(BlacklistIp blacklistIp) {
        return BlacklistIpResponseDto.builder()
                .id(blacklistIp.getId())
                .ipAddress(blacklistIp.getIpAddress())
                .reason(blacklistIp.getReason())
                .createdAt(blacklistIp.getCreatedAt())
                .build();
    }
}
