package page.clab.api.domain.auth.accountAccessLog.adapter.out.persistence;

import org.mapstruct.Mapper;
import page.clab.api.domain.auth.accountAccessLog.domain.AccountAccessLog;

@Mapper(componentModel = "spring")
public interface AccountAccessLogMapper {

    AccountAccessLogJpaEntity toEntity(AccountAccessLog accountAccessLog);

    AccountAccessLog toDomain(AccountAccessLogJpaEntity jpaEntity);
}
