package com.backend.payring.security;

import com.backend.payring.service.JWTServiceImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTServiceImpl jwtServiceImpl;

    public JWTAuthenticationFilter(JWTServiceImpl jwtServiceImpl) {
        this.jwtServiceImpl = jwtServiceImpl;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String token = extractToken(request);

        if (token != null && jwtServiceImpl.isTokenValid(token)) {
            String userId = jwtServiceImpl.getUserIdFromToken(token);
            if (userId != null) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userId, null, null);
                SecurityContextHolder.getContext().setAuthentication(authentication); // 인증 정보 설정
            }
        } else {
            System.out.println("Invalid token or no token found");
        }

        chain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            return token;
        }
        return null;
    }
}
