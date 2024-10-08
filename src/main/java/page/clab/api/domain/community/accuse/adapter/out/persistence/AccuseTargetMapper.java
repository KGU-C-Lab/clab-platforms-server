package page.clab.api.domain.community.accuse.adapter.out.persistence;

import org.mapstruct.Mapper;
import page.clab.api.domain.community.accuse.domain.AccuseTarget;

@Mapper(componentModel = "spring")
public interface AccuseTargetMapper {

    AccuseTargetJpaEntity toEntity(AccuseTarget target);

    AccuseTarget toDomain(AccuseTargetJpaEntity entity);
}
