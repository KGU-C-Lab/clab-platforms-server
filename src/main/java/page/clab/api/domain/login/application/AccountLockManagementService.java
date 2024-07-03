package page.clab.api.domain.login.application;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.login.application.port.in.AccountLockManagementUseCase;
import page.clab.api.domain.login.application.port.out.LoadAccountLockInfoPort;
import page.clab.api.domain.login.application.port.out.RegisterAccountLockInfoPort;
import page.clab.api.domain.login.domain.AccountLockInfo;
import page.clab.api.domain.login.exception.LoginFailedException;
import page.clab.api.domain.login.exception.MemberLockedException;
import page.clab.api.domain.member.application.port.in.MemberLookupUseCase;
import page.clab.api.global.common.slack.application.SlackService;
import page.clab.api.global.common.slack.domain.SecurityAlertType;

@Service
@RequiredArgsConstructor
public class AccountLockManagementService implements AccountLockManagementUseCase {

    private final MemberLookupUseCase memberLookupUseCase;
    private final SlackService slackService;
    private final LoadAccountLockInfoPort loadAccountLockInfoPort;
    private final RegisterAccountLockInfoPort registerAccountLockInfoPort;

    @Value("${security.login-attempt.max-failures}")
    private int maxLoginFailures;

    @Value("${security.login-attempt.lock-duration-minutes}")
    private int lockDurationMinutes;

    @Transactional
    @Override
    public void handleAccountLockInfo(String memberId) throws MemberLockedException, LoginFailedException {
        ensureMemberExists(memberId);
        AccountLockInfo accountLockInfo = ensureAccountLockInfo(memberId);
        validateAccountLockStatus(accountLockInfo);
        accountLockInfo.unlockAccount();
        registerAccountLockInfoPort.save(accountLockInfo);
    }

    @Transactional
    @Override
    public void handleLoginFailure(HttpServletRequest request, String memberId) throws MemberLockedException, LoginFailedException {
        ensureMemberExists(memberId);
        AccountLockInfo accountLockInfo = ensureAccountLockInfo(memberId);
        validateAccountLockStatus(accountLockInfo);
        accountLockInfo.incrementLoginFailCount();
        if (accountLockInfo.shouldBeLocked(maxLoginFailures)) {
            accountLockInfo.lockAccount(lockDurationMinutes);
            sendSlackLoginFailureNotification(request, memberId);
        }
        registerAccountLockInfoPort.save(accountLockInfo);
    }

    private AccountLockInfo ensureAccountLockInfo(String memberId) {
        return loadAccountLockInfoPort.findByMemberId(memberId)
                .orElseGet(() -> registerAccountLockInfoPort.save(AccountLockInfo.create(memberId)));
    }

    private void ensureMemberExists(String memberId) throws LoginFailedException {
        if (memberLookupUseCase.getMemberById(memberId).isEmpty()) {
            throw new LoginFailedException();
        }
    }

    private void validateAccountLockStatus(AccountLockInfo accountLockInfo) throws MemberLockedException {
        if (accountLockInfo.isCurrentlyLocked()) {
            throw new MemberLockedException();
        }
    }

    private void sendSlackLoginFailureNotification(HttpServletRequest request, String memberId) {
        String memberName = memberLookupUseCase.getMemberBasicInfoById(memberId).getMemberName();
        if (memberLookupUseCase.getMemberDetailedInfoById(memberId).isAdminRole()) {
            request.setAttribute("member", memberId + " " + memberName);
            slackService.sendSecurityAlertNotification(request, SecurityAlertType.REPEATED_LOGIN_FAILURES, "로그인 실패 횟수 초과로 계정이 잠겼습니다.");
        }
    }
}