package com.portfolio.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
        registry.addMapping("/**") // Applies this config to all endpoints
                .allowedOrigins(allowedOrigin) // Authorizes only this frontend origin (React)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Permits common HTTP methods
                .allowedHeaders("*") // Accepts all request headers
                .allowCredentials(true); // Allows sending credentials (cookies, auth headers) if needed

    }

    @Override
    public void addResourceHandlers(
            org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}
