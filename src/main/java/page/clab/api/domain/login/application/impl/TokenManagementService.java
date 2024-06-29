package page.clab.api.domain.login.application.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.login.application.LoginService;
import page.clab.api.domain.login.application.RedisTokenService;
import page.clab.api.domain.login.domain.RedisToken;
import page.clab.api.domain.login.dto.request.LoginRequestDto;
import page.clab.api.domain.login.dto.request.TwoFactorAuthenticationRequestDto;
import page.clab.api.domain.login.dto.response.LoginResult;
import page.clab.api.domain.login.dto.response.TokenHeader;
import page.clab.api.domain.login.dto.response.TokenInfo;
import page.clab.api.domain.login.exception.LoginFaliedException;
import page.clab.api.domain.login.exception.MemberLockedException;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.global.auth.exception.TokenForgeryException;
import page.clab.api.global.auth.exception.TokenMisuseException;
import page.clab.api.global.auth.jwt.JwtTokenProvider;
import page.clab.api.global.util.HttpReqResUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
@Qualifier("tokenManagementService")
public class TokenManagementService implements LoginService {

    private final JwtTokenProvider jwtTokenProvider;

    private final MemberLookupService memberLookupService;

    private final RedisTokenService redisTokenService;

    @Transactional
    @Override
    public TokenHeader reissueToken(HttpServletRequest request) {
        String refreshToken = jwtTokenProvider.resolveToken(request);
        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);
        RedisToken redisToken = redisTokenService.getRedisTokenByRefreshToken(refreshToken);

        validateMemberExistence(authentication);
        validateToken(redisToken);

        TokenInfo newTokenInfo = jwtTokenProvider.generateToken(redisToken.getId(), redisToken.getRole());
        redisTokenService.saveRedisToken(redisToken.getId(), redisToken.getRole(), newTokenInfo, redisToken.getIp());
        return TokenHeader.create(newTokenInfo);
    }

    @Override
    public List<String> retrieveCurrentLoggedInUsers() {
        return redisTokenService.getCurrentLoggedInUsers();
    }

    private void validateMemberExistence(Authentication authentication) {
        String id = authentication.getName();
        if (memberLookupService.getMemberById(id) == null) {
            throw new TokenForgeryException("존재하지 않는 회원에 대한 토큰입니다.");
        }
    }

    private void validateToken(RedisToken redisToken) {
        String clientIpAddress = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
        if (!redisToken.isSameIp(clientIpAddress)) {
            redisTokenService.deleteRedisTokenByAccessToken(redisToken.getAccessToken());
            throw new TokenMisuseException("[" + clientIpAddress + "] 토큰 발급 IP와 다른 IP에서 발급을 시도하여 토큰을 삭제하였습니다.");
        }
    }

    @Override
    public LoginResult login(HttpServletRequest request, LoginRequestDto requestDto) throws LoginFaliedException, MemberLockedException {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public LoginResult authenticate(HttpServletRequest request, TwoFactorAuthenticationRequestDto requestDto) throws LoginFaliedException, MemberLockedException {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public String resetAuthenticator(String memberId) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public String revokeToken(String memberId) {
        redisTokenService.deleteRedisTokenByMemberId(memberId);
        return memberId;
    }
}
