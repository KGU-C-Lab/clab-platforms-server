package page.clab.api.domain.memberManagement.member.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class MemberBirthdayResponseDto {

    private String id;
    private String name;
    private LocalDate birth;
    private String imageUrl;
}
