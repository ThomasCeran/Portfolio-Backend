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

import com.portfolio.backend.security.JwtFilter;
import com.portfolio.backend.service.CustomUserDetailsService;

/**
 * Main security configuration class for the application.
 * <p>
 * Configures HTTP security, authentication, JWT filter, and password encoding.
 * </p>
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtFilter jwtFilter;

    /**
     * Constructs the SecurityConfig with required dependencies.
     *
     * @param customUserDetailsService The custom service for loading user-specific
     *                                 data from the database.
     * @param jwtFilter                The custom JWT filter for token-based
     *                                 authentication.
     */
    public SecurityConfig(CustomUserDetailsService customUserDetailsService, JwtFilter jwtFilter) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtFilter = jwtFilter;
    }

    /**
     * Configures the main security filter chain.
     * <ul>
     * <li>Disables CSRF for REST APIs.</li>
     * <li>Restricts <code>/api/admin/**</code> routes to users with the ADMIN
     * role.</li>
     * <li>Allows unrestricted access to <code>/api/messages/**</code> and
     * <code>/api/auth/**</code>.</li>
     * <li>Requires authentication for all other routes.</li>
     * <li>Applies the custom JWT filter before Spring's
     * UsernamePasswordAuthenticationFilter.</li>
     * </ul>
     *
     * @param http The HttpSecurity configuration object.
     * @return The configured SecurityFilterChain.
     * @throws Exception In case of configuration errors.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors() 
                .and()
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/messages/**").permitAll()
                        .anyRequest().authenticated())
                .userDetailsService(customUserDetailsService)
                .addFilterBefore(jwtFilter,
                        org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * Declares the password encoder bean using BCrypt.
     * All passwords will be hashed and verified using BCrypt.
     *
     * @return A PasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Declares the AuthenticationManager bean for authentication needs.
     *
     * @param authenticationConfiguration The Spring AuthenticationConfiguration.
     * @return The AuthenticationManager instance.
     * @throws Exception In case of configuration errors.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
