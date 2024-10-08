package page.clab.api.domain.community.accuse.adapter.out.persistence;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import page.clab.api.domain.community.accuse.domain.Accuse;

@Mapper(componentModel = "spring", uses = {AccuseTargetMapper.class})
public interface AccuseMapper {

    @Mapping(source = "target", target = "target")
    AccuseJpaEntity toEntity(Accuse accuse);

    @Mapping(source = "target", target = "target")
    Accuse toDomain(AccuseJpaEntity entity);
}
