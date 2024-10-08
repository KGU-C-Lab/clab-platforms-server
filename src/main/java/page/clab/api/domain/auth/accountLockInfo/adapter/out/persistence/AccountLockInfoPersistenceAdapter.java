package page.clab.api.domain.auth.accountLockInfo.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.auth.accountLockInfo.application.port.out.RegisterAccountLockInfoPort;
import page.clab.api.domain.auth.accountLockInfo.application.port.out.RemoveAccountLockInfoPort;
import page.clab.api.domain.auth.accountLockInfo.application.port.out.RetrieveAccountLockInfoPort;
import page.clab.api.domain.auth.accountLockInfo.domain.AccountLockInfo;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AccountLockInfoPersistenceAdapter implements
        RegisterAccountLockInfoPort,
        RetrieveAccountLockInfoPort,
        RemoveAccountLockInfoPort {

    private final AccountLockInfoRepository accountLockInfoRepository;
    private final AccountLockInfoMapper accountLockInfoMapper;

    @Override
    public AccountLockInfo save(AccountLockInfo accountLockInfo) {
        AccountLockInfoJpaEntity jpaEntity = accountLockInfoMapper.toEntity(accountLockInfo);
        AccountLockInfoJpaEntity savedEntity = accountLockInfoRepository.save(jpaEntity);
        return accountLockInfoMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<AccountLockInfo> findByMemberId(String memberId) {
        return accountLockInfoRepository.findByMemberId(memberId).map(accountLockInfoMapper::toDomain);
    }

    @Override
    public void delete(AccountLockInfo accountLockInfo) {
        AccountLockInfoJpaEntity jpaEntity = accountLockInfoMapper.toEntity(accountLockInfo);
        accountLockInfoRepository.delete(jpaEntity);
    }

    public Page<AccountLockInfo> findByLockUntil(LocalDateTime banDate, Pageable pageable) {
        Page<AccountLockInfoJpaEntity> jpaEntities = accountLockInfoRepository.findByLockUntil(banDate, pageable);
        return jpaEntities.map(accountLockInfoMapper::toDomain);
    }
}
