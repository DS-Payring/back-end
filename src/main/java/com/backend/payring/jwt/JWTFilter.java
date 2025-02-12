package com.backend.payring.jwt;


import com.backend.payring.code.ErrorCode;
import com.backend.payring.config.SecurityConfig;
import com.backend.payring.entity.UserEntity;
import com.backend.payring.util.TokenErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final SecurityConfig securityConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        List<String> permitAllUrls = securityConfig.getPermitAllUrls();

        boolean isPermitAllRequest = permitAllUrls.stream()
                .anyMatch(url -> new AntPathRequestMatcher(url).matches(request));

        String authorization= request.getHeader("Authorization");

        //Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            if (!isPermitAllRequest) {
                TokenErrorResponse.sendErrorResponse(response, ErrorCode.TOKEN_MISSING);
                return;
            }
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.split(" ")[1];

        try {
            //토큰 소멸 시간 검증
            if (jwtUtil.isExpired(token)) {
                TokenErrorResponse.sendErrorResponse(response, ErrorCode.TOKEN_EXPIRED);
                return;
            }

            String userId = jwtUtil.getUsername(token);

            UserEntity user = UserEntity.builder()
                    .id(Long.parseLong(userId))
                    .build();

            Authentication authToken = new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(authToken);

        } catch (ExpiredJwtException e) {
            TokenErrorResponse.sendErrorResponse(response, ErrorCode.TOKEN_EXPIRED);
            return;
        } catch (Exception e) {
            log.info(e.getMessage());
            TokenErrorResponse.sendErrorResponse(response, ErrorCode.INVALID_ACCESS_TOKEN);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
