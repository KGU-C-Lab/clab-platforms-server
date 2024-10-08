package page.clab.api.domain.members.activityPhoto.adapter.out.persistence;

import org.mapstruct.Mapper;
import page.clab.api.domain.members.activityPhoto.domain.ActivityPhoto;

@Mapper(componentModel = "spring")
public interface ActivityPhotoMapper {

    ActivityPhotoJpaEntity toEntity(ActivityPhoto activityPhoto);

    ActivityPhoto toDomain(ActivityPhotoJpaEntity entity);
}
