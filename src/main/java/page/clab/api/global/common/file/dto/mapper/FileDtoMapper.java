package page.clab.api.global.common.file.dto.mapper;

import page.clab.api.global.common.file.domain.UploadedFile;
import page.clab.api.global.common.file.dto.request.DeleteFileRequestDto;
import page.clab.api.global.common.file.dto.response.FileInfo;
import page.clab.api.global.common.file.dto.response.UploadedFileResponseDto;

import java.io.File;
import java.util.Date;
import java.util.List;

public class FileDtoMapper {

    public static DeleteFileRequestDto toDeletedFileRequestDto(String url) {
        return DeleteFileRequestDto.builder()
                .url(url)
                .build();
    }

    public static FileInfo toFileInfo(File file) {
        if (file == null || !file.exists() || !file.isFile()) {
            return null;
        }

        return FileInfo.builder()
                .fileName(file.getName())
                .fileSizeInBytes(file.length())
                .creationDate(new Date(file.lastModified()))
                .modificationDate(new Date(file.lastModified()))
                .build();
    }

    public static List<UploadedFileResponseDto> toUploadedFileResponseDto(List<UploadedFile> uploadedFiles) {
        return uploadedFiles.stream()
                .map(FileDtoMapper::toUploadedFileResponseDto)
                .toList();
    }

    public static UploadedFileResponseDto toUploadedFileResponseDto(UploadedFile uploadedFile) {
        return UploadedFileResponseDto.builder()
                .fileUrl(uploadedFile.getUrl())
                .originalFileName(uploadedFile.getOriginalFileName())
                .storagePeriod(uploadedFile.getStoragePeriod())
                .createdAt(uploadedFile.getCreatedAt())
                .build();
    }
}
