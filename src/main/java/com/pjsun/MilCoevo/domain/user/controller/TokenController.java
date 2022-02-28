package com.pjsun.MilCoevo.domain.user.controller;

import com.pjsun.MilCoevo.domain.user.dto.TokenDto;
import com.pjsun.MilCoevo.dto.ResponseDto;
import com.pjsun.MilCoevo.exception.InvalidTokenException;
import com.pjsun.MilCoevo.jwt.JwtFilter;
import com.pjsun.MilCoevo.jwt.JwtTokenProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class TokenController {

    private final JwtTokenProvider tokenProvider;

    @Value("${success.common.response}")
    private String SUCCESS_RESPONSE;

    @GetMapping("/token/expired")
    public String auth() {
        throw new InvalidTokenException();
    }

    @GetMapping("/token/refresh")
    public TokenDto refreshAuth(
            HttpServletRequest request, HttpServletResponse response) {

        String jwt = resolveToken(request);

        if(StringUtils.hasText(jwt) && tokenProvider.validationToken(jwt)) {
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            TokenDto token = tokenProvider.createToken(authentication);

            response.addHeader("Auth", "Bearer " + token.getAccessToken());
            response.addHeader("Refresh", "Bearer " + token.getRefreshToken());
            response.setContentType("application/json;charset=UTF-8");

            ResponseDto data = new ResponseDto(SUCCESS_RESPONSE, jwt);

            return token;
        }
        throw new InvalidTokenException();
    }

    // HttpRequest Header 에서 token 가져오기
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(JwtFilter.AUTHORIZATION_HEADER);

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
