package page.clab.api.domain.hiring.recruitment.adapter.out.persistence;

import org.mapstruct.Mapper;
import page.clab.api.domain.hiring.recruitment.domain.Recruitment;

@Mapper(componentModel = "spring")
public interface RecruitmentMapper {

    RecruitmentJpaEntity toEntity(Recruitment recruitment);

    Recruitment toDomain(RecruitmentJpaEntity entity);
}
