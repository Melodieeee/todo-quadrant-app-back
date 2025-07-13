package com.melody.todoquadrantappback.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.melody.todoquadrantappback.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler oauth2LoginSuccessHandler;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService,
                          OAuth2LoginSuccessHandler oauth2LoginSuccessHandler) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.oauth2LoginSuccessHandler = oauth2LoginSuccessHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint((request, response, authException) -> {
                    String uri = request.getRequestURI();
                    if (uri.startsWith("/api/")) { // 401 未登入就訪問 /api/user/info
                        System.out.println("Unauthorized access to API: " + uri);
                        String traceId = request.getRequestURI() + "-" + System.currentTimeMillis();
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.setContentType("application/json");
                        response.getWriter().write("""
                            {
                                "code": "E401",
                                "error": "Unauthorized",
                                "message": "User not authenticated.",
                                "traceId": "%s",
                                "timestamp": "%s"
                            }
                        """.formatted(traceId, java.time.Instant.now()));
                    } else {
                        System.out.println("Redirecting to OAuth2 login for: " + uri);
                        response.sendRedirect("/oauth2/authorization/google");
                    }
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    String uri = request.getRequestURI(); //403 登入但無權限（角色限制)
                    if (uri.startsWith("/api/")) {
                        String traceId = uri + "-" + System.currentTimeMillis();
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        response.setContentType("application/json");
                        response.getWriter().write("""
                            {
                                "code": "E403",
                                "error": "Forbidden",
                                "message": "Access denied.",
                                "traceId": "%s",
                                "timestamp": "%s"
                            }
                        """.formatted(traceId, java.time.Instant.now()));
                        System.out.println("Forbidden access: " + uri);
                    }
                })
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/error", "/oauth2/**").permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo
                        .userService(customOAuth2UserService))
                .successHandler(oauth2LoginSuccessHandler)
            );

        return http.build();
    }

}
