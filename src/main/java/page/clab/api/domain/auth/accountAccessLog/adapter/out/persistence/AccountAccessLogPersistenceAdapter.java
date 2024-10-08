package page.clab.api.domain.auth.accountAccessLog.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import page.clab.api.domain.auth.accountAccessLog.application.port.out.RegisterAccountAccessLogPort;
import page.clab.api.domain.auth.accountAccessLog.domain.AccountAccessLog;

@Component
@RequiredArgsConstructor
public class AccountAccessLogPersistenceAdapter implements
        RegisterAccountAccessLogPort {

    private final AccountAccessLogRepository accountAccessLogRepository;
    private final AccountAccessLogMapper accountAccessLogMapper;

    @Override
    public AccountAccessLog save(AccountAccessLog accountAccessLog) {
        AccountAccessLogJpaEntity jpaEntity = accountAccessLogMapper.toEntity(accountAccessLog);
        AccountAccessLogJpaEntity savedEntity = accountAccessLogRepository.save(jpaEntity);
        return accountAccessLogMapper.toDomain(savedEntity);
    }
}
