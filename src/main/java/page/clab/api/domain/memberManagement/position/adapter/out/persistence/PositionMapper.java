package page.clab.api.domain.memberManagement.position.adapter.out.persistence;

import org.mapstruct.Mapper;
import page.clab.api.domain.memberManagement.position.domain.Position;

@Mapper(componentModel = "spring")
public interface PositionMapper {

    PositionJpaEntity toEntity(Position position);

    Position toDomain(PositionJpaEntity jpaEntity);
}
