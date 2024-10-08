package page.clab.api.domain.auth.accountLockInfo.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccountLockInfoResponseDto {

    private String id;
    private String name;
}
