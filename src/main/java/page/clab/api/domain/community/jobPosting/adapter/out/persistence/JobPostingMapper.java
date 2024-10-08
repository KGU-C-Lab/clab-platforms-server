package page.clab.api.domain.community.jobPosting.adapter.out.persistence;

import org.mapstruct.Mapper;
import page.clab.api.domain.community.jobPosting.domain.JobPosting;

@Mapper(componentModel = "spring")
public interface JobPostingMapper {

    JobPostingJpaEntity toEntity(JobPosting domain);

    JobPosting toDomain(JobPostingJpaEntity entity);
}
