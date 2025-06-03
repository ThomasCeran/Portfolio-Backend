package com.portfolio.backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Utility class for generating and validating JWT tokens.
 */
@Component
public class JwtUtil {

    private final Key secretKey;
    private final long expirationTime;

    /**
     * Constructor for JwtUtil. Uses Spring @Value to inject properties.
     * 
     * @param secret         The secret key as a string (should be long enough for
     *                       HS256).
     * @param expirationTime Token expiration in milliseconds.
     */
    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expirationTime) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationTime = expirationTime;
    }

    /**
     * Generates a JWT token for a given username and optional claims.
     * 
     * @param username    The username.
     * @param extraClaims Optional extra claims (can be null).
     * @return The JWT token as a String.
     */
    public String generateToken(String username, Map<String, Object> extraClaims) {
        Map<String, Object> claims = new HashMap<>();
        if (extraClaims != null) {
            claims.putAll(extraClaims);
        }
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Generates a JWT token for a given username without extra claims.
     */
    public String generateToken(String username) {
        return generateToken(username, null);
    }

    /**
     * Extracts username (subject) from JWT token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a specific claim from JWT token.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Validates a token: correct signature, not expired, username matches.
     */
    public boolean validateToken(String token, String username) {
        try {
            String extractedUsername = extractUsername(token);
            return (extractedUsername.equals(username) && !isTokenExpired(token));
        } catch (JwtException | IllegalArgumentException e) {
            // Token expired, malformed, or invalid signature
            return false;
        }
    }

    /**
     * Checks if the token is expired.
     */
    public boolean isTokenExpired(String token) {
        try {
            Date expiration = extractClaim(token, Claims::getExpiration);
            return expiration.before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    /**
     * Parses the token and returns all claims.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Get extra claims (custom data) from JWT.
     */
    public Map<String, Object> extractAllExtraClaims(String token) {
        Claims claims = extractAllClaims(token);
        Map<String, Object> extraClaims = new HashMap<>(claims);
        extraClaims.remove(Claims.SUBJECT);
        extraClaims.remove(Claims.EXPIRATION);
        extraClaims.remove(Claims.ISSUED_AT);
        return extraClaims;
    }
}
