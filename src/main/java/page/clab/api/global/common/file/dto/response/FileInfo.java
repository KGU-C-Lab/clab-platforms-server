package page.clab.api.global.common.file.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class FileInfo {

    private String fileName;
    private Long fileSizeInBytes;
    private Date creationDate;
    private Date modificationDate;
}
