package com.pjsun.MilCoevo.jwt;

import antlr.Token;
import com.pjsun.MilCoevo.exception.InvalidTokenException;
import com.pjsun.MilCoevo.exception.NoTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/*
* 토큰 인증정보를 SecurityContext 저장
* */
@Slf4j
public class JwtFilter extends GenericFilter{

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private final JwtTokenProvider jwtTokenProvider;

    public JwtFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException, InvalidTokenException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String jwt = resolveToken(httpServletRequest);
        String requestURI = httpServletRequest.getRequestURI();

        log.debug("{}", jwt);
        if(StringUtils.hasText(jwt) && jwtTokenProvider.validationToken(jwt)) {
            // 토큰 -> Authentication
            Authentication authentication = jwtTokenProvider.getAuthentication(jwt);

            // Authentication securityContext 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.info("Security Context에 '{}' 인증정보를 저장했습니다. uri: {}", authentication.getName(), requestURI);
        } else {
            log.debug("Token이 없습니다.");
        }
        chain.doFilter(request, response);
    }

    // HttpRequest Header 에서 token 가져오기
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
