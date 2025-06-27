package com.melody.todoquadrantappback.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig {
    @Value("${FRONTEND_URL}")
    private  String frontendUrl;
//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        System.out.println("Configuring CORS with frontend URL: " + frontendUrl);
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**")
//                        .allowedOrigins(frontendUrl)
//                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                        .allowedHeaders("*")
//                        .allowCredentials(true);
//            }
//        };
//    }
        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
            System.out.println("Configuring CORS with frontend URL: " + frontendUrl);

            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(List.of(frontendUrl)); // 設定允許的前端 URL
            config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            config.setAllowedHeaders(List.of("*")); // 允許所有標頭
            config.setAllowCredentials(true); // 允許 cookie
            config.setMaxAge(3600L); // 預檢請求快取時間（可選）

            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", config);
            return source;
        }

        @Bean
        public WebMvcConfigurer webMvcConfigurer() {
            return new WebMvcConfigurer() {
                // 可以保留這個空的實作，必要時加其他 MVC 設定
            };
        }
}

