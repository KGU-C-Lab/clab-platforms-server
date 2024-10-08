package page.clab.api.domain.members.activityPhoto.application.dto.mapper;

import page.clab.api.domain.members.activityPhoto.application.dto.request.ActivityPhotoRequestDto;
import page.clab.api.domain.members.activityPhoto.application.dto.response.ActivityPhotoResponseDto;
import page.clab.api.domain.members.activityPhoto.domain.ActivityPhoto;
import page.clab.api.global.common.file.domain.UploadedFile;
import page.clab.api.global.common.file.dto.mapper.FileDtoMapper;

import java.util.List;

public class ActivityPhotoDtoMapper {

    public static ActivityPhoto toActivityPhoto(ActivityPhotoRequestDto requestDto, List<UploadedFile> uploadedFiles) {
        return ActivityPhoto.builder()
                .title(requestDto.getTitle())
                .uploadedFiles(uploadedFiles)
                .date(requestDto.getDate())
                .isPublic(false)
                .isDeleted(false)
                .build();
    }

    public static ActivityPhotoResponseDto toActivityPhotoResponseDto(ActivityPhoto activityPhoto) {
        return ActivityPhotoResponseDto.builder()
                .id(activityPhoto.getId())
                .title(activityPhoto.getTitle())
                .files(FileDtoMapper.toUploadedFileResponseDto(activityPhoto.getUploadedFiles()))
                .date(activityPhoto.getDate())
                .isPublic(activityPhoto.getIsPublic())
                .createdAt(activityPhoto.getCreatedAt())
                .build();
    }
}
