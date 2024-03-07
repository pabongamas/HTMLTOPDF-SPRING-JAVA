package com.microservicepdf.microservicepdf.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        // Configura las políticas CORS según tus necesidades
        config.addAllowedOrigin("http://php-legacy.avance.org.co:7070/");
        config.addAllowedMethod("*"); // Permite todos los métodos HTTP
        config.addAllowedHeader("*"); // Permite todos los encabezados

        source.registerCorsConfiguration("/microserviceSigno/**", config);
        return new CorsFilter(source);
    }
}
