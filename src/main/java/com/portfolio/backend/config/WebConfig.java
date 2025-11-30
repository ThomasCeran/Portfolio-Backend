package com.portfolio.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.util.StringUtils;

/**
 * WebConfig class
 * Configures global Cross-Origin Resource Sharing (CORS) settings for the
 * application.
 * 
 * This configuration allows the frontend (React app running on
 * http://localhost:5173)
 * to communicate with the backend (Spring Boot API) during local development.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${cors.allowed-origin}")
    private String allowedOrigin;

    /**
     * Configures CORS mappings for all API endpoints.
     *
     * @param registry the CORS registry provided by Spring
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String[] origins = StringUtils.tokenizeToStringArray(allowedOrigin, ",");
        registry.addMapping("/**")
                .allowedOrigins(origins)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);

    }

    @Override
    public void addResourceHandlers(
            org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}
