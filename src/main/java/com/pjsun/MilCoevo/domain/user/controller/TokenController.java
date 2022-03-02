package com.pjsun.MilCoevo.domain.user.controller;

import com.pjsun.MilCoevo.domain.user.dto.TokenDto;
import com.pjsun.MilCoevo.dto.ResponseDto;
import com.pjsun.MilCoevo.exception.InvalidTokenException;
import com.pjsun.MilCoevo.exception.NoRefreshTokenException;
import com.pjsun.MilCoevo.jwt.JwtFilter;
import com.pjsun.MilCoevo.jwt.JwtTokenProvider;
import com.pjsun.MilCoevo.util.CookieUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/token")
public class TokenController {

    private final JwtTokenProvider tokenProvider;

    @Value("${success.common.response}")
    private String SUCCESS_RESPONSE;

    @GetMapping("/expired")
    public String auth() {
        throw new InvalidTokenException();
    }

    @ApiOperation(value = "Refresh Token 재발급")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "accessToken", required = true, dataType = "String", paramType = "header")
    })
    @GetMapping("/refresh")
    public ResponseEntity<ResponseDto> refreshAuth(
            HttpServletRequest request, HttpServletResponse response) {

        // refresh token 유효시 access token 발급
        Cookie cookie = CookieUtils.getCookie(request, CookieUtils.REFRESH_TOKEN)
                .orElseThrow(() -> {
                    throw new NoRefreshTokenException("Refresh Token is not existed");
                });
        String refreshToken = cookie.getValue();

        // refresh token 만료시 로그인 하라고 보내기
        if(StringUtils.hasText(refreshToken) && tokenProvider.validationToken(refreshToken)) {
            Authentication authentication = tokenProvider.getAuthentication(refreshToken);
            TokenDto newToken = tokenProvider.createToken(authentication);

            ResponseDto data = new ResponseDto(SUCCESS_RESPONSE, newToken.getAccessToken());

            return new ResponseEntity<>(data, HttpStatus.OK);
        }
        throw new NoRefreshTokenException("Refresh Token is not existed");
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
