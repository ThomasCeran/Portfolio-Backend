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
 * <p>
 * Supports custom claims (e.g., user roles) and handles all typical
 * operations such as token creation, parsing, validation, and extraction
 * of custom claims.
 * </p>
 */
@Component
public class JwtUtil {

    private final Key secretKey;
    private final long expirationTime;

    /**
     * Constructs the JwtUtil with the secret key and token expiration time.
     *
     * @param secret         The secret key used for signing tokens (must be at
     *                       least 256 bits for HS256).
     * @param expirationTime The expiration time in milliseconds for generated
     *                       tokens.
     */
    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expirationTime) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationTime = expirationTime;
    }

    /**
     * Generates a JWT token for the given username (subject) and additional claims.
     *
     * @param username    The user's unique identifier (subject, typically email).
     * @param extraClaims A map of additional claims to include (e.g., roles); can
     *                    be null.
     * @return A signed JWT token string.
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
     * Generates a JWT token for the given username without extra claims.
     *
     * @param username The user's unique identifier (subject).
     * @return A signed JWT token string.
     */
    public String generateToken(String username) {
        return generateToken(username, null);
    }

    /**
     * Extracts the username (subject) from a JWT token.
     *
     * @param token The JWT token.
     * @return The username (subject).
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a specific claim from a JWT token using a claim resolver.
     *
     * @param token          The JWT token.
     * @param claimsResolver A function that takes Claims and returns the desired
     *                       value.
     * @param <T>            The type of the claim.
     * @return The extracted claim value.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Validates a token by checking the signature, expiration, and that the
     * username matches.
     *
     * @param token    The JWT token.
     * @param username The expected username (subject).
     * @return True if valid, false otherwise.
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
     * Checks whether a token has expired.
     *
     * @param token The JWT token.
     * @return True if expired, false otherwise.
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
     * Parses the JWT token and returns all claims.
     *
     * @param token The JWT token.
     * @return The Claims object with all token data.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Extracts all custom (extra) claims from the JWT, excluding standard claims.
     *
     * @param token The JWT token.
     * @return A map of custom claims (e.g., roles).
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
