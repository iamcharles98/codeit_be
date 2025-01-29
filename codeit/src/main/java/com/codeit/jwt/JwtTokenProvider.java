package com.codeit.jwt;

import com.codeit.util.BaseException;
import com.codeit.util.ErrorType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtTokenProvider {
    private final SecretKey key;
    private final long accessExpireTime = 24 * 60 * 60 * 1000L;
    private final long refreshExpireTime = 24 * 60 * 60 * 1000L;
    private final JwtParser jwtParser;
    private final String ISSUE;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret,
                            @Value("${jwt.issuer}") String issue) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
        this.ISSUE = issue;
    }

    public String createAccessToken(Long id) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", id);
        return createToken(accessExpireTime, payload);
    }

    public String createRefreshToken(Long id) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", id);
        return createToken(refreshExpireTime, payload);
    }

    private String createToken(long expireTime, Map<String, Object> payload) {
        Date now = new Date();
        return Jwts.builder()
                .setIssuer(ISSUE)
                .setClaims(payload)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expireTime))
                .signWith(key)
                .compact();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = getValidClaim(token);
        return Long.parseLong(claims.get("userId").toString());
    }

    private Claims getValidClaim(String token) {
        try {
            log.info("Validating token {}", token);
            return parseClaim(token);
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("token expired", e);
        } catch (Exception e) {
            throw new RuntimeException("invalid token", e);
        }
    }

    private Claims parseClaim(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }

    // 토큰 정보를 검증하는 메서드
    public void validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            log.info("Invalid JWT Token", e);
            throw new BaseException(ErrorType.INVALID_JWT);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
            throw new BaseException(ErrorType.EXPIRED_JWT);
        } catch (Exception e) {
            log.error("Unexpected error while validating JWT token", e);
            throw new RuntimeException("Unexpected error while validating JWT token", e);
        }
    }
}
