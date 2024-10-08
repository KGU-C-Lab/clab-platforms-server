package page.clab.api.domain.auth.accountLockInfo.adapter.out.persistence;

import org.mapstruct.Mapper;
import page.clab.api.domain.auth.accountLockInfo.domain.AccountLockInfo;

@Mapper(componentModel = "spring")
public interface AccountLockInfoMapper {

    AccountLockInfoJpaEntity toEntity(AccountLockInfo accountLockInfo);

    AccountLockInfo toDomain(AccountLockInfoJpaEntity jpaEntity);
}
