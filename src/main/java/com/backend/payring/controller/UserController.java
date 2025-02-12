package com.backend.payring.controller;

import com.backend.payring.code.ErrorCode;
import com.backend.payring.code.ResponseCode;
import com.backend.payring.dto.response.ResponseDTO;
import com.backend.payring.dto.user.UserDTO;
import com.backend.payring.entity.UserEntity;
import com.backend.payring.exception.UserException;
import com.backend.payring.repository.UserRepository;
import com.backend.payring.service.UserServiceImpl;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @PostMapping("/email/verify/send")
    public ResponseEntity<?> sendVerificationCode(@RequestParam String email) throws MessagingException {
        userServiceImpl.sendVerificationCode(email);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_SEND_EMAIL.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_SEND_EMAIL, null));
    }

    @PostMapping("/email/verify")
    public ResponseEntity<?> verifyEmail(@RequestBody @Valid UserDTO .EmailVerificationReq req) {
        userServiceImpl.verifyEmail(req);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_VERIFY_EMAIL.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_VERIFY_EMAIL, null));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid UserDTO.SignUpReq req) {
//        if (userServiceImpl.existsByEmail(req.getEmail())) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).body(new UserDTO.Res("이미 가입된 메일입니다."));
//        }
        UserDTO.Res res = userServiceImpl.signup(req);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_SIGNUP.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_SIGNUP, res));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserDTO.LoginReq req) {
        UserDTO.Res res = userServiceImpl.login(req);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_LOGIN.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_LOGIN, res));
    }

    @GetMapping("/profile")
    public String getUserProfile(@AuthenticationPrincipal UserEntity user) {
        if (user == null) {
            throw new UserException(ErrorCode.USER_NOT_FOUND);
        }
        return "유저: " + user.getId();
    }
}
