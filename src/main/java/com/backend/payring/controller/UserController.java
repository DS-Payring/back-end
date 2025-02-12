package com.backend.payring.controller;

import com.backend.payring.code.ErrorCode;
import com.backend.payring.code.ResponseCode;
import com.backend.payring.dto.response.ResponseDTO;
import com.backend.payring.dto.user.UserDTO;
import com.backend.payring.entity.UserEntity;
import com.backend.payring.exception.UserException;
import com.backend.payring.repository.UserRepository;
import com.backend.payring.service.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(
            summary = "이메일 인증번호 요청 API",
            description = "이메일 인증번호 코드를 요청합니다. 이때 유저가 생성됩니다."
    )
    @PostMapping("/email/verify/send")
    public ResponseEntity<?> sendVerificationCode(@RequestParam String email) throws MessagingException {
        userServiceImpl.sendVerificationCode(email);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_SEND_EMAIL.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_SEND_EMAIL, null));
    }


    @Operation(
            summary = "인증번호 인증 API",
            description = "인증번호를 입력하여 이메일 인증을 완료합니다."
    )
    @PostMapping("/email/verify")
    public ResponseEntity<?> verifyEmail(@RequestBody @Valid UserDTO .EmailVerificationReq req) {
        userServiceImpl.verifyEmail(req);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_VERIFY_EMAIL.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_VERIFY_EMAIL, null));
    }

    @Operation(
            summary = "회원가입 API",
            description = "회원가입을 진행합니다."
    )
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

    @Operation(
            summary = "로그인 API",
            description = "로그인하여 토큰을 얻습니다."
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserDTO.LoginReq req) {
        UserDTO.Res res = userServiceImpl.login(req);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_LOGIN.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_LOGIN, res));
    }

    @Operation(
            summary = "(테스트용) 유저id 확인 API",
            description = "유저아이디가 떠야 합니다."
    )
    @GetMapping("/profile")
    public String getUserProfile(@AuthenticationPrincipal UserEntity user) {
        if (user == null) {
            throw new UserException(ErrorCode.USER_NOT_FOUND);
        }
        return "유저: " + user.getId();
    }
}
