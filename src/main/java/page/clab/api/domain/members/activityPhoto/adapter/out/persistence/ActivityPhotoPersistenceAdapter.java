package page.clab.api.domain.members.activityPhoto.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.members.activityPhoto.application.port.out.RegisterActivityPhotoPort;
import page.clab.api.domain.members.activityPhoto.application.port.out.RetrieveActivityPhotoPort;
import page.clab.api.domain.members.activityPhoto.domain.ActivityPhoto;
import page.clab.api.global.exception.NotFoundException;

@Component
@RequiredArgsConstructor
public class ActivityPhotoPersistenceAdapter implements
        RegisterActivityPhotoPort,
        RetrieveActivityPhotoPort {

    private final ActivityPhotoRepository activityPhotoRepository;
    private final ActivityPhotoMapper activityPhotoMapper;

    @Override
    public ActivityPhoto save(ActivityPhoto activityPhoto) {
        ActivityPhotoJpaEntity entity = activityPhotoMapper.toEntity(activityPhoto);
        ActivityPhotoJpaEntity savedEntity = activityPhotoRepository.save(entity);
        return activityPhotoMapper.toDomain(savedEntity);
    }

    @Override
    public ActivityPhoto getById(Long activityPhotoId) {
        return activityPhotoRepository.findById(activityPhotoId)
                .map(activityPhotoMapper::toDomain)
                .orElseThrow(() -> new NotFoundException("[ActivityPhoto] id: " + activityPhotoId + "에 해당하는 활동 사진이 존재하지 않습니다."));
    }

    @Override
    public Page<ActivityPhoto> findByConditions(Boolean isPublic, Pageable pageable) {
        return activityPhotoRepository.findByConditions(isPublic, pageable)
                .map(activityPhotoMapper::toDomain);
    }
}
