package com.backend.payring.controller;

import com.backend.payring.dto.user.UserDTO;
import com.backend.payring.repository.UserRepository;
import com.backend.payring.service.JWTServiceImpl;
import com.backend.payring.service.UserServiceImpl;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userServiceImpl;
    private final UserRepository userRepository;
    private final JWTServiceImpl jwtServiceImpl;

    @PostMapping("/email/verify/send")
    public ResponseEntity<Void> sendVerificationCode(@RequestParam String email) throws MessagingException {
        userServiceImpl.sendVerificationCode(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/email/verify")
    public ResponseEntity<Void> verifyEmail(@RequestBody @Valid UserDTO .EmailVerificationReq req) {
        userServiceImpl.verifyEmail(req);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDTO.Res> signUp(@RequestBody @Valid UserDTO.SignUpReq req) {
//        if (userServiceImpl.existsByEmail(req.getEmail())) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).body(new UserDTO.Res("이미 가입된 메일입니다."));
//        }
        UserDTO.Res res = userServiceImpl.signup(req);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO.Res> login(@RequestBody @Valid UserDTO.LoginReq req) {
        UserDTO.Res res = userServiceImpl.login(req);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/profile")
    public String getUserProfile(@RequestHeader("Authorization") String token) {
        String jwtToken = token.replace("Bearer ", "");
        String userId = jwtServiceImpl.getUserIdFromToken(jwtToken);
        return "유저: " + userId;
    }
}
