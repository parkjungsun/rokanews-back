package com.pjsun.MilCoevo.jwt;

import com.pjsun.MilCoevo.dto.ResponseDto;
import com.pjsun.MilCoevo.exception.InvalidTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/* JWT 유효하지 않을 때 */
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        log.debug("Exception: JwtAuthenticationEntryPoint");
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
