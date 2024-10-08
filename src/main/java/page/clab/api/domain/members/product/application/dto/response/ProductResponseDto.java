package page.clab.api.domain.members.product.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ProductResponseDto {

    private Long id;
    private String name;
    private String description;
    private String url;
    private LocalDateTime createdAt;
}
