package page.clab.api.domain.auth.blacklistIp.adapter.out.persistence;

import org.mapstruct.Mapper;
import page.clab.api.domain.auth.blacklistIp.domain.BlacklistIp;

@Mapper(componentModel = "spring")
public interface BlacklistIpMapper {

    BlacklistIpJpaEntity toEntity(BlacklistIp blacklistIp);

    BlacklistIp toDomain(BlacklistIpJpaEntity entity);
}
