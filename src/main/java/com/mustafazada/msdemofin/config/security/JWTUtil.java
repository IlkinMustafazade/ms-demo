package com.mustafazada.msdemofin.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JWTUtil {
    @NonFinal
    @Value("${SECRET_KEY}")
    String secretKey;

    Logger logger;

    public String createToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("test", "test");
        return generateToken(claims, userDetails.getUsername());
    }

    private String generateToken(Map<String, Object> claims, String username) {
        logger.info("Creating Token..............................");
        logger.info("Generating JWT token for user: {}", username);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 5 * 60))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extractAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token).getBody();
    }

    public <T> T extractClaimsFromToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public String getUsernameFromToken(String token) {
        return extractClaimsFromToken(token, Claims::getSubject);
    }

    public Date getDateFromToken(String token) {
        return extractClaimsFromToken(token, Claims::getExpiration);
    }

    public Boolean isTokenExpired(String token, UserDetails userDetails) {
        return (!expiredToken(token) && (getUsernameFromToken(token).equals(userDetails.getUsername())));
    }

    private boolean expiredToken(String token) {
        return getDateFromToken(token).before(new Date());
    }
}
