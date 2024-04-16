package page.clab.api.domain.board.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.board.domain.BoardCategory;

import java.time.LocalDateTime;

@Getter
@Builder
public class BoardCategoryResponseDto {

    private Long id;

    private String category;

    private String writerId;

    private String writerName;

    private String title;

    private String imageUrl;

    private LocalDateTime createdAt;

    public static BoardCategoryResponseDto toDto(Board board) {
        return BoardCategoryResponseDto.builder()
                .id(board.getId())
                .category(board.getCategory().getKey())
                .writerId(board.isWantAnonymous() ? null : board.getMember().getId())
                .writerName(board.isWantAnonymous() ? board.getNickname() : board.getMember().getName())
                .title(board.getTitle())
                .imageUrl(board.getImageUrl())
                .createdAt(board.getCreatedAt())
                .build();
    }

}
