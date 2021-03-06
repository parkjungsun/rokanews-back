package com.pjsun.MilCoevo.config;

import com.pjsun.MilCoevo.interceptor.CheckLeaderInterceptor;
import com.pjsun.MilCoevo.interceptor.CheckMemberInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public CheckMemberInterceptor checkMemberInterceptor() {
        return new CheckMemberInterceptor();
    }

    @Bean
    public CheckLeaderInterceptor checkLeaderInterceptor() {
        return new CheckLeaderInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(checkMemberInterceptor())
                .addPathPatterns("/api/news/**", "/api/schedule/**",
                        "/api/absence/**", "/api/purchase/**", "/api/notice/**");

        registry.addInterceptor(checkLeaderInterceptor())
                .addPathPatterns("/api/group/**/member/**",
                        "/api/group/**/name","/api/group/**/code")
                .excludePathPatterns("/api/group/**/member");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")  // CORS 적용 URL 패턴
                .allowedOrigins("http://localhost:8080", "http://localhost:3000")    // 자원 공유 origin
                .allowedMethods("GET", "POST", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Set-Cookie")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
