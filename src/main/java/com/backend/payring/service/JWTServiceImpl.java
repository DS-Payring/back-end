package com.backend.payring.service;

import com.backend.payring.code.ErrorCode;
import com.backend.payring.entity.UserEntity;

import com.backend.payring.exception.UserException;
import com.backend.payring.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.internal.Function;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JWTServiceImpl implements JWTService{

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private String expiration;

    private final UserRepository userRepository;

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserEntity user) {
        long now = new Date().getTime();
        Date accessTokenExpiresIn = new Date(now + Long.parseLong(expiration));

        return Jwts.builder()
                .setSubject(user.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(accessTokenExpiresIn)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getExpiration().after(new Date());
        } catch (io.jsonwebtoken.SignatureException e) {
            System.out.println("Invalid JWT signature: " + e.getMessage());  // 서명 관련 오류
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            System.out.println("Malformed JWT token: " + e.getMessage());  // 잘못된 JWT 형식
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            System.out.println("Expired JWT token: " + e.getMessage());  // 만료된 JWT 토큰
        } catch (Exception e) {
            System.out.println("Invalid token: " + e.getMessage());  // 기타 예외
        }
        return false;
    }

    public String getUserIdFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject();
        } catch (Exception e) {
            throw new UserException(ErrorCode.INVALID_ACCESS_TOKEN);
        }
    }

    public Boolean isExpired(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)  // setSigningKey() 사용
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration()
                    .before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }


}
