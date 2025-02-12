package com.backend.payring.config;

import com.backend.payring.jwt.LoginFilter;
import com.backend.payring.jwt.JWTFilter;
import com.backend.payring.jwt.JWTUtil;
import com.backend.payring.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JWTUtil jwtUtil;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Getter
    private final List<String> permitAllUrls = List.of(
            "/login", "/api/auth/**",
            "/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**",
            "/api/health-check"
    );

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable);
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);

        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(permitAllUrls.toArray(new String[0])).permitAll()
                        .anyRequest().authenticated());

        http.addFilterBefore(new JWTFilter(jwtUtil, this), LoginFilter.class);

        return http.build();
    }
}

