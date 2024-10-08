package page.clab.api.domain.members.membershipFee.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.members.membershipFee.domain.MembershipFeeStatus;

import java.time.LocalDateTime;

@Getter
@Builder
public class MembershipFeeResponseDto {

    private Long id;
    private String memberId;
    private String memberName;
    private String category;
    private String account;
    private Long amount;
    private String content;
    private String imageUrl;
    private MembershipFeeStatus status;
    private LocalDateTime createdAt;
}
