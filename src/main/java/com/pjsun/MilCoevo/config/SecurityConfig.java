package com.pjsun.MilCoevo.config;

import com.pjsun.MilCoevo.domain.user.service.CustomOAuth2UserService;
import com.pjsun.MilCoevo.jwt.JwtAccessDeniedHandler;
import com.pjsun.MilCoevo.jwt.JwtAuthenticationEntryPoint;
import com.pjsun.MilCoevo.jwt.JwtSecurityConfig;
import com.pjsun.MilCoevo.jwt.JwtTokenProvider;
import com.pjsun.MilCoevo.oauth.HttpCookieOAuth2AuthorizationRequestRepository;
import com.pjsun.MilCoevo.oauth.OAuth2FailureHandler;
import com.pjsun.MilCoevo.oauth.OAuth2SuccessHandler;
import lombok.ToString;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/*
* Spring Security Filter Chain 사용
* - WebSecurityConfigurerAdapter 상속 + @EnableWebSecurity (기본적인 웹 보안 활성화)
* EnableGlobalMethodSecurity
* - @PreAuthorize 메소드 단위로 추가
* */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler successHandler;
    private final OAuth2FailureHandler failureHandler;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    public SecurityConfig(
            CustomOAuth2UserService customOAuth2UserService,
            OAuth2SuccessHandler successHandler,
            OAuth2FailureHandler failureHandler,
            HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository,

            JwtTokenProvider jwtTokenProvider,
            JwtAccessDeniedHandler jwtAccessDeniedHandler,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint
    ) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                "/static/css/**", "/static/js/**",
                "/h2-console/**", "/favicon.ico", "/error");
        web.ignoring().antMatchers(
                "/v2/api-docs", "/configuration/ui", "/swagger-resources",
                "/configuration/security", "/swagger-ui.html", "/webjars/**",
                "/swagger/**", "/swagger-ui/**");
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors().and()
                .csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()

                .headers()
                    .frameOptions()
                    .sameOrigin()
                    .and()

                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()

                .exceptionHandling()
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()

                .authorizeRequests()
                    .antMatchers("/swagger-resources/**").permitAll()
                    .antMatchers("/api/token/**").permitAll()
                    .antMatchers("/api/user/register").permitAll()
                    .antMatchers("/api/user/login").permitAll()
                    .antMatchers("/oauth2/**").permitAll()
                    .antMatchers("/login/**").permitAll()
                    .antMatchers("/api/**").hasRole("USER")
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .anyRequest()
                    .authenticated()
                    .and()

                .oauth2Login()
                    // OAuth 로그인 진입점
                    .authorizationEndpoint()
                    .authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository)
                    .baseUri("/oauth2/authorization")
                    .and()

                    // 인증서버에서 Redirect point
                    .redirectionEndpoint()
                    .baseUri("/**/oauth2/code/**")
                    .and()

                    // 인증서버에서 받은 유저정보 확인
                    .userInfoEndpoint()
                    .userService(customOAuth2UserService)
                    .and()

                    // 성공시, 실패시
                    .successHandler(successHandler)
                    .failureHandler((AuthenticationFailureHandler) failureHandler);

        httpSecurity
                .apply(new JwtSecurityConfig(jwtTokenProvider));
    }
}