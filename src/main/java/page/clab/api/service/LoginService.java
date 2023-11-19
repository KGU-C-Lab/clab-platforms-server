package page.clab.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import page.clab.api.auth.jwt.JwtTokenProvider;
import page.clab.api.exception.DuplicateLoginException;
import page.clab.api.exception.LoginFaliedException;
import page.clab.api.exception.MemberLockedException;
import page.clab.api.type.dto.LoginRequestDto;
import page.clab.api.type.dto.RefreshTokenDto;
import page.clab.api.type.dto.TokenInfo;
import page.clab.api.type.entity.Member;
import page.clab.api.type.entity.RedisToken;
import page.clab.api.type.etc.LoginAttemptResult;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

    private final JwtTokenProvider jwtTokenProvider;

    private final LoginFailInfoService loginFailInfoService;

    private final MemberService memberService;

    private final LoginAttemptLogService loginAttemptLogService;

    private final RedisTokenService redisTokenService;

    @Transactional
    public TokenInfo login(HttpServletRequest httpServletRequest, LoginRequestDto loginRequestDto) throws LoginFaliedException, MemberLockedException {
        String id = loginRequestDto.getId();
        String password = loginRequestDto.getPassword();
        Member member = memberService.getMemberById(id);
        if (member == null) {
            throw new LoginFaliedException("존재하지 않는 아이디입니다.");
        }
        boolean loginSuccess = barunLogin(id, password);
        TokenInfo tokenInfo = null;
        if (loginSuccess) {
            loginFailInfoService.handleLoginFailInfo(id);
            tokenInfo = jwtTokenProvider.generateToken(id, member.getRole());
            memberService.setLastLoginTime(id);
            loginAttemptLogService.createLoginAttemptLog(httpServletRequest, id, LoginAttemptResult.SUCCESS);
            redisTokenService.saveRedisToken(member.getId(), member.getRole(), tokenInfo, httpServletRequest.getRemoteAddr());
        } else {
            loginAttemptLogService.createLoginAttemptLog(httpServletRequest, id, LoginAttemptResult.FAILURE);
            loginFailInfoService.updateLoginFailInfo(id);
        }
        return tokenInfo;
    }

    private boolean barunLogin(String id, String password) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("email", id);
        formData.add("password", password);

        WebClient webClient = WebClient.builder()
                .baseUrl("https://barun.kyonggi.ac.kr")
                .build();

        String response = webClient
                .post()
                .uri("/ko/process/member/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(formData)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> jsonResponse = objectMapper.readValue(response, Map.class);
            return (boolean) jsonResponse.get("success");
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public TokenInfo reissue(HttpServletRequest request, RefreshTokenDto refreshTokenDto) {
        String token = jwtTokenProvider.resolveToken(request);
        if (token != null && jwtTokenProvider.validateToken(token)) {
            String currentIpAddress = request.getRemoteAddr();
            RedisToken redisToken = redisTokenService.getRedisToken(token);
            if (redisToken != null && redisToken.getIp().equals(currentIpAddress)
                    && redisToken.getRefreshToken().equals(refreshTokenDto.getRefreshToken())) {
                TokenInfo tokenInfo = jwtTokenProvider.generateToken(redisToken.getId(), redisToken.getRole());
                redisTokenService.saveRedisToken(redisToken.getId(), redisToken.getRole(), tokenInfo, currentIpAddress);
                return tokenInfo;
            } else {
                redisTokenService.deleteRedisTokenByAccessToken(token);
                log.info("[{}/{}] : 중복 로그인이 감지되어 로그아웃 처리되었습니다.", redisToken.getId(), redisToken.getIp());
                throw new DuplicateLoginException("중복 로그인이 감지되어 로그아웃 처리되었습니다.");
            }
        }
        return null;
    }
    
}
