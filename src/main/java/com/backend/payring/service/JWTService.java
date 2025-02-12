package com.backend.payring.service;

import com.backend.payring.entity.UserEntity;

public interface JWTService {
    String generateToken(UserEntity user);

    String extractEmail(String token);

    boolean isTokenValid(String token);

    String getUserIdFromToken(String token);
}
