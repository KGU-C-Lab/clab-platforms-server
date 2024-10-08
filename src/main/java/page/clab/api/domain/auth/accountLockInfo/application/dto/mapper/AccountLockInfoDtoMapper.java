package page.clab.api.domain.auth.accountLockInfo.application.dto.mapper;

import page.clab.api.domain.auth.accountLockInfo.application.dto.response.AccountLockInfoResponseDto;
import page.clab.api.domain.auth.accountLockInfo.domain.AccountLockInfo;

public class AccountLockInfoDtoMapper {

    public static AccountLockInfoResponseDto toAccountLockInfoResponseDto(AccountLockInfo accountLockInfo, String memberName) {
        return AccountLockInfoResponseDto.builder()
                .id(accountLockInfo.getMemberId())
                .name(memberName)
                .build();
    }
}
