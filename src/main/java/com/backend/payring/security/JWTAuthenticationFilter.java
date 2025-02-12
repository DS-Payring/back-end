package com.backend.payring.security;

import com.backend.payring.code.ErrorCode;
import com.backend.payring.exception.UserException;
import com.backend.payring.service.JWTServiceImpl;
import com.backend.payring.util.TokenErrorResponse;
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

        //request에서 Authorization 헤더를 찾음
        String authorization= request.getHeader("Authorization");

        //Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = authorization.split(" ")[1];

        try {

        } c

        if (token != null && jwtServiceImpl.isTokenValid(token)) {
            Long userId = jwtServiceImpl.getUserIdFromToken(token);
            if (userId != null) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userId, null, null);
                SecurityContextHolder.getContext().setAuthentication(authentication); // 인증 정보 설정
            }
        } else {
            System.out.println("token이 없음");
            // TokenErrorResponse.sendErrorResponse(response, ErrorCode.TOKEN_MISSING);
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
