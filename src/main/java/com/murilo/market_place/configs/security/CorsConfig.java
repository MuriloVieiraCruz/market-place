package com.murilo.market_place.configs.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration publicConfig = new CorsConfiguration();
        publicConfig.setAllowedOrigins(List.of("*"));
        publicConfig.setAllowedMethods(List.of("*"));
        publicConfig.setAllowedHeaders(List.of("*"));
        source.registerCorsConfiguration("/public/**", publicConfig);

        CorsConfiguration apiConfig = new CorsConfiguration();
        apiConfig.setAllowedOrigins(List.of("https://app.example.com"));
        apiConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        apiConfig.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
        source.registerCorsConfiguration("/api/**", apiConfig);

        CorsConfiguration adminConfig = new CorsConfiguration();
        adminConfig.setAllowedOrigins(List.of("https://admin.example.com"));
        adminConfig.setAllowedMethods(Arrays.asList("GET", "POST"));
        adminConfig.setAllowedHeaders(List.of("Content-Type"));
        source.registerCorsConfiguration("/admin/**", adminConfig);

        return source;
    }
}