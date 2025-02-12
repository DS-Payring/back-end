package com.backend.payring.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@Slf4j
public class JWTUtil {

    private final SecretKey secretKey;

    public JWTUtil(@Value("${jwt.secret}") String secret) {
        byte[] keyBytes = secret.getBytes();
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    @Value("${jwt.expiration}")
    private String expiration;

    public String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("userId", String.class);
    }

    public Boolean isExpired(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration()
                    .before(new Date());
        } catch (ExpiredJwtException e) {
            log.info(e.getMessage());
            return true;
        } catch (JwtException e) {
            log.info(e.getMessage());
            return true;
        }
    }

    public String createJwt(String userId) {
        return Jwts.builder()
                .claim("userId", userId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(expiration)))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
}
