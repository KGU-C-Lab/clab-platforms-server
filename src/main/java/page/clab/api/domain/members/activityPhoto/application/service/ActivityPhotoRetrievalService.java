package page.clab.api.domain.members.activityPhoto.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.members.activityPhoto.application.dto.mapper.ActivityPhotoDtoMapper;
import page.clab.api.domain.members.activityPhoto.application.dto.response.ActivityPhotoResponseDto;
import page.clab.api.domain.members.activityPhoto.application.port.in.RetrieveActivityPhotoUseCase;
import page.clab.api.domain.members.activityPhoto.application.port.out.RetrieveActivityPhotoPort;
import page.clab.api.domain.members.activityPhoto.domain.ActivityPhoto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class ActivityPhotoRetrievalService implements RetrieveActivityPhotoUseCase {

    private final RetrieveActivityPhotoPort retrieveActivityPhotoPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<ActivityPhotoResponseDto> retrieveActivityPhotos(Boolean isPublic, Pageable pageable) {
        Page<ActivityPhoto> activityPhotos = retrieveActivityPhotoPort.findByConditions(isPublic, pageable);
        return new PagedResponseDto<>(activityPhotos.map(ActivityPhotoDtoMapper::toActivityPhotoResponseDto));
    }
}
