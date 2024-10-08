package page.clab.api.domain.library.book.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BookResponseDto {

    private Long id;
    private String borrowerId;
    private String borrowerName;
    private String category;
    private String title;
    private String author;
    private String publisher;
    private String imageUrl;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
