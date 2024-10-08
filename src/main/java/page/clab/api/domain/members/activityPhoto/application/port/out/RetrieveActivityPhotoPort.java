package page.clab.api.domain.members.activityPhoto.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.members.activityPhoto.domain.ActivityPhoto;

public interface RetrieveActivityPhotoPort {

    ActivityPhoto getById(Long activityPhotoId);

    Page<ActivityPhoto> findByConditions(Boolean isPublic, Pageable pageable);
}
