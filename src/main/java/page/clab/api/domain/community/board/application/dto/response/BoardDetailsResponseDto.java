package page.clab.api.domain.community.board.application.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import page.clab.api.global.common.file.dto.response.UploadedFileResponseDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class BoardDetailsResponseDto {

    private Long id;
    private String writerId;
    private String writerName;
    private Long writerRoleLevel;
    private String writerImageUrl;
    private String category;
    private String title;
    private String content;
    private List<UploadedFileResponseDto> files;
    private String imageUrl;

    @JsonProperty("isOwner")
    private Boolean isOwner;
    private List<BoardEmojiCountResponseDto> emojiInfos;
    private LocalDateTime createdAt;
}
