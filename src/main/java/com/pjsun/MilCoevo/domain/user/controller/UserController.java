package com.pjsun.MilCoevo.domain.user.controller;

import com.pjsun.MilCoevo.dto.ResponseDto;
import com.pjsun.MilCoevo.domain.user.dto.TokenDto;
import com.pjsun.MilCoevo.domain.user.dto.UserLoginRequestDto;
import com.pjsun.MilCoevo.domain.user.dto.UserRegisterRequestDto;
import com.pjsun.MilCoevo.domain.user.User;
import com.pjsun.MilCoevo.exception.DuplicateUserException;
import com.pjsun.MilCoevo.jwt.JwtFilter;
import com.pjsun.MilCoevo.jwt.JwtTokenProvider;
import com.pjsun.MilCoevo.domain.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Value("${error.common.bindingError}")
    private String BINDING_ERROR_EXCEPTION;

    @Value("${success.common.response}")
    private String SUCCESS_RESPONSE;

    @ApiOperation(value = "로그인", notes = "이메일, 패스워드 기반 로그인")
    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login(
            @Validated @RequestBody UserLoginRequestDto requestDto,
            BindingResult bindingResult) throws UsernameNotFoundException {

        if(bindingResult.hasErrors()) {
            ResponseDto error = new ResponseDto(BINDING_ERROR_EXCEPTION, bindingResult);
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        String jwt = loginByEmailAndPassword(requestDto.getEmail(), requestDto.getPassword());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        ResponseDto data = new ResponseDto(SUCCESS_RESPONSE, new TokenDto(jwt));

        return new ResponseEntity<>(data, httpHeaders, HttpStatus.OK);
    }

    @ApiOperation(value = "회원가입", notes = "이메일, 패스워드 기반 회원가입")
    @PostMapping("/register")
    public ResponseEntity<ResponseDto> register(
            @Validated @RequestBody UserRegisterRequestDto requestDto,
            BindingResult bindingResult) throws DuplicateUserException {

        if(bindingResult.hasErrors()) {
            ResponseDto error = new ResponseDto(BINDING_ERROR_EXCEPTION, bindingResult);
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        userService.register(requestDto.getEmail(), requestDto.getPassword());

        ResponseDto data = new ResponseDto(SUCCESS_RESPONSE);

        return new ResponseEntity<>(data, HttpStatus.CREATED);
    }

    private String loginByEmailAndPassword(String email, String password) throws UsernameNotFoundException {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);

        /* authenticate 실행시 UserDetailsService -> loadUserByUsername 실행됨 */
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtTokenProvider.createToken(authentication);
    }
}
