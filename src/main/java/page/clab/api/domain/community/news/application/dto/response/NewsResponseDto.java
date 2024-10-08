package page.clab.api.domain.community.news.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class NewsResponseDto {

    private Long id;
    private String title;
    private String category;
    private String articleUrl;
    private LocalDate date;
    private LocalDateTime createdAt;
}
