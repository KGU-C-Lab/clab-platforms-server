package page.clab.api.domain.auth.accountLockInfo.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.auth.accountLockInfo.application.dto.mapper.AccountLockInfoDtoMapper;
import page.clab.api.domain.auth.accountLockInfo.application.dto.response.AccountLockInfoResponseDto;
import page.clab.api.domain.auth.accountLockInfo.application.port.in.RetrieveBannedMembersUseCase;
import page.clab.api.domain.auth.accountLockInfo.application.port.out.RetrieveAccountLockInfoPort;
import page.clab.api.domain.auth.accountLockInfo.domain.AccountLockInfo;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.common.dto.PagedResponseDto;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BanMembersRetrievalService implements RetrieveBannedMembersUseCase {

    private final RetrieveAccountLockInfoPort retrieveAccountLockInfoPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<AccountLockInfoResponseDto> retrieveBanMembers(Pageable pageable) {
        LocalDateTime banDate = LocalDateTime.of(9999, 12, 31, 23, 59);
        Page<AccountLockInfo> banMembers = retrieveAccountLockInfoPort.findByLockUntil(banDate, pageable);
        return new PagedResponseDto<>(banMembers.map(accountLockInfo -> {
            String memberName = externalRetrieveMemberUseCase.getMemberBasicInfoById(accountLockInfo.getMemberId()).getMemberName();
            return AccountLockInfoDtoMapper.toAccountLockInfoResponseDto(accountLockInfo, memberName);
        }));
    }
}
