package page.clab.api.domain.memberManagement.award.adapter.out.persistence;

import org.mapstruct.Mapper;
import page.clab.api.domain.memberManagement.award.domain.Award;

@Mapper(componentModel = "spring")
public interface AwardMapper {

    AwardJpaEntity toEntity(Award award);

    Award toDomain(AwardJpaEntity entity);
}
