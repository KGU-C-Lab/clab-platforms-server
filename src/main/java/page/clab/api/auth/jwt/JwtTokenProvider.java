package page.clab.api.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import page.clab.api.auth.exception.TokenValidateException;
import page.clab.api.service.RedisTokenService;
import page.clab.api.type.dto.TokenInfo;
import page.clab.api.type.etc.Role;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private final Key key;

    private static final long ACCESS_TOKEN_DURATION = 30L * 60L * 1000L; // 30분

    private static final long REFRESH_TOKEN_DURATION = 40L * 60L * 1000L; // 40분

    private final RedisTokenService redisTokenService;

    public JwtTokenProvider(@Value("${jwt.secret-key}") String secretKey, RedisTokenService redisTokenService) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.redisTokenService = redisTokenService;
    }

    public TokenInfo generateToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date expiry = new Date();
        Date accessTokenExpiry = new Date(expiry.getTime() + (ACCESS_TOKEN_DURATION));
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("role", authorities)
                .setExpiration(accessTokenExpiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        Date refreshTokenExpiry = new Date(expiry.getTime() + (REFRESH_TOKEN_DURATION));
        String refreshToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("role", authorities)
                .setExpiration(refreshTokenExpiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        redisTokenService.saveTokenInfo(authentication.getName(), accessToken, refreshToken);

        return TokenInfo.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public TokenInfo generateToken(String id, Role role) {
        Date expiry = new Date();
        Date accessTokenExpiry = new Date(expiry.getTime() + (ACCESS_TOKEN_DURATION));
        String accessToken = Jwts.builder()
                .setSubject(id)
                .claim("role", role)
                .setExpiration(accessTokenExpiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        Date refreshTokenExpiry = new Date(expiry.getTime() + (REFRESH_TOKEN_DURATION));
        String refreshToken = Jwts.builder()
                .setSubject(id)
                .claim("role", role)
                .setExpiration(refreshTokenExpiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        redisTokenService.saveTokenInfo(id, accessToken, refreshToken);

        return TokenInfo.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);
        log.debug("claims : {}", claims);
        log.debug("accessToken : {}", accessToken);

        if (claims.get("role") == null) {
            throw new TokenValidateException("권한 정보가 없는 토큰입니다.");
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("role").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.");
        }
        return false;
    }

    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

}