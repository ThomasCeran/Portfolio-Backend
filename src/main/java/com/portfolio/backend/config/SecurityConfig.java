package com.portfolio.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.portfolio.backend.service.CustomUserDetailsService;

/**
 * Main security configuration class for the application.
 * Configures HTTP security, authentication, and password encoding.
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    /**
     * Constructor for injecting the custom UserDetailsService.
     * 
     * @param customUserDetailsService Custom service for loading user-specific
     *                                 data.
     */
    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    /**
     * Configures the main security filter chain.
     * - Disables CSRF for REST APIs.
     * - Restricts /api/admin/** routes to ADMIN role.
     * - Allows unrestricted access to /api/messages/**.
     * - Requires authentication for all other routes.
     * - Uses the custom UserDetailsService for user lookups.
     *
     * @param http HttpSecurity configuration object.
     * @return Configured SecurityFilterChain.
     * @throws Exception In case of configuration errors.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/messages/**").permitAll()
                        .anyRequest().authenticated())
                .userDetailsService(customUserDetailsService);
        return http.build();
    }

    /**
     * Declares the password encoder bean using BCrypt.
     * All passwords will be hashed and verified using BCrypt.
     *
     * @return PasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Declares the AuthenticationManager bean for authentication needs.
     *
     * @param authenticationConfiguration Spring AuthenticationConfiguration.
     * @return AuthenticationManager instance.
     * @throws Exception In case of configuration errors.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
