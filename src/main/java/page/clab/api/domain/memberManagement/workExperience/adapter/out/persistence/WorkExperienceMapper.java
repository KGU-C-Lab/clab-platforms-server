package page.clab.api.domain.memberManagement.workExperience.adapter.out.persistence;

import org.mapstruct.Mapper;
import page.clab.api.domain.memberManagement.workExperience.domain.WorkExperience;

@Mapper(componentModel = "spring")
public interface WorkExperienceMapper {

    WorkExperienceJpaEntity toEntity(WorkExperience workExperience);

    WorkExperience toDomain(WorkExperienceJpaEntity jpaEntity);
}
