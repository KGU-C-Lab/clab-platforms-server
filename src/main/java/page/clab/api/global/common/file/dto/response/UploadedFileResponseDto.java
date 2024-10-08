package page.clab.api.global.common.file.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UploadedFileResponseDto {

    private String fileUrl;
    private String originalFileName;
    private Long storagePeriod;
    private LocalDateTime createdAt;
}
