package com.portfolio.backend.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.portfolio.backend.service.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * JWT authentication filter for processing incoming HTTP requests.
 * <p>
 * This filter extracts the JWT token from the Authorization header, validates it,
 * loads the user and their roles from the database using {@link CustomUserDetailsService},
 * and sets the authentication in the Spring Security context.
 * </p>
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;
    private final TokenBlacklistService tokenBlacklistService;

    /**
     * Constructor for dependency injection.
     *
     * @param jwtUtil                  the utility class for handling JWT operations
     * @param customUserDetailsService the service for loading user details from the
     *                                 database
     */
    public JwtFilter(JwtUtil jwtUtil, CustomUserDetailsService customUserDetailsService,
            TokenBlacklistService tokenBlacklistService) {
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    /**
     * Processes each HTTP request to extract, validate, and authenticate the JWT
     * token.
     *
     * @param request  the incoming HTTP servlet request
     * @param response the outgoing HTTP servlet response
     * @param chain    the filter chain to continue processing
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        // Check if the Authorization header is present and starts with "Bearer "
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        // Extract the JWT token (remove "Bearer " prefix)
        String token = authorizationHeader.substring(7);

        if (tokenBlacklistService.isRevoked(token)) {
            logger.debug("Token is revoked, skipping authentication");
            chain.doFilter(request, response);
            return;
        }

        String username = null;
        try {
            username = jwtUtil.extractUsername(token);
        } catch (Exception e) {
            logger.warn("Unable to extract username from JWT: {}", e.getMessage());
        }

        // If username is extracted and no authentication is set yet
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Load user details (including roles) from the database
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            // Validate the token against the username
            if (jwtUtil.validateToken(token, username)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                logger.debug("JWT authentication set for user: {}", username);
            } else {
                logger.warn("Invalid JWT token for user: {}", username);
            }
        }

        // Continue with the filter chain
        chain.doFilter(request, response);
    }
}
