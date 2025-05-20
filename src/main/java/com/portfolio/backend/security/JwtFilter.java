package com.portfolio.backend.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    private final JwtUtil jwtUtil;

    // Injection du JwtUtil par constructeur (best practice pour l’injection de
    // dépendance)
    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        // Vérifie si le header Authorization est bien présent et commence par "Bearer "
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        // Récupère le token JWT sans le préfixe "Bearer "
        String token = authorizationHeader.substring(7);

        String username = null;
        try {
            username = jwtUtil.extractUsername(token);
        } catch (Exception e) {
            logger.warn("Impossible d'extraire le username du JWT : {}", e.getMessage());
        }

        // Si on a extrait un username et qu'aucune authentification n'est encore
        // présente
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Ici on crée un UserDetails simple avec rôle USER, à adapter si besoin
            UserDetails userDetails = User.withUsername(username)
                    .password("") // inutile ici car l’authentification est déjà faite via JWT
                    .roles("USER")
                    .build();

            // Vérifie la validité du token
            if (jwtUtil.validateToken(token, username)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                logger.debug("Authentification JWT définie pour l'utilisateur : {}", username);
            } else {
                logger.warn("Token JWT invalide pour l'utilisateur : {}", username);
            }
        }

        // Poursuit la chaîne des filtres, qu'il y ait eu authentification ou non
        chain.doFilter(request, response);
    }
}
