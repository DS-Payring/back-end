package com.backend.payring.service;

import com.backend.payring.dto.user.UserDTO;

public interface UserService {
    void sendVerificationCode(String email);

    void verifyEmail(UserDTO.EmailVerificationReq req);

    boolean existsByEmail(String email);

    UserDTO.Res signup(UserDTO.SignUpReq req);

    UserDTO.Res login(UserDTO.LoginReq req);
}
