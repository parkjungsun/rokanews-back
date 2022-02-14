package com.pjsun.MilCoevo.jwt;

/*
* JWT 토큰 생성 및 검증을 위한 Provider Class
* */

import com.pjsun.MilCoevo.exception.InvalidTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private final String secret;

    private final long tokenValidTime;

    private Key key;

    private static final String AUTH_KEY = "auth";

    public JwtTokenProvider(
            @Value("${security.jwt.secretKey}") String secret,
            @Value("${security.jwt.validTime}") long tokenValidTime) {
        this.secret = secret;
        this.tokenValidTime = tokenValidTime;
    }

    @PostConstruct
    protected void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // Authentication -> 토큰
    public String createToken(Authentication authentication) {
        log.debug("Authentication to Token");
        // 권한 flat
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidTime);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTH_KEY, authorities) // payload
                .signWith(key, SignatureAlgorithm.HS512) // signature
                .setExpiration(validity) // expire
                .compact();
    }

    // 토큰 -> Authentication
    public Authentication getAuthentication(String token) {
        log.debug("Token to Authentication");
        // 토큰 -> claim
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key).build()
                .parseClaimsJws(token).getBody();

        // claim -> get -> 권한정보
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTH_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // 권한정보 -> User
        User principal = new User(claims.getSubject(), "", authorities);

        // 권한정보, User, token -> Authentication
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    // token 유효성 검사
    public boolean validationToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.debug("잘못된 JWT 서명입니다.");
            throw new InvalidTokenException("Invalid Token Signature");
        } catch (ExpiredJwtException e) {
            log.debug("만료된 JWT 토큰입니다.");
            throw new InvalidTokenException("Expired Token");
        } catch (UnsupportedJwtException e) {
            log.debug("지원하지 않는 JWT 토큰입니다.");
            throw new InvalidTokenException("Unsupported Token");
        } catch (IllegalArgumentException e) {
            log.debug("JWT 토큰이 잘못되었습니다.");
            throw new InvalidTokenException("Invalid Token");
        }
    }
}
