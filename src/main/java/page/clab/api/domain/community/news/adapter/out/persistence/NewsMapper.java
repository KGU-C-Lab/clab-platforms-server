package page.clab.api.domain.community.news.adapter.out.persistence;

import org.mapstruct.Mapper;
import page.clab.api.domain.community.news.domain.News;

@Mapper(componentModel = "spring")
public interface NewsMapper {

    NewsJpaEntity toEntity(News news);

    News toDomain(NewsJpaEntity jpaEntity);
}
