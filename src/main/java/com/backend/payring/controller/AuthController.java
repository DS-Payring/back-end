package com.backend.payring.controller;

import com.backend.payring.code.ResponseCode;
import com.backend.payring.dto.response.ResponseDTO;
import com.backend.payring.dto.user.UserDTO;
import com.backend.payring.repository.UserRepository;
import com.backend.payring.service.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

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
    public ResponseEntity<?> verifyEmail(@RequestBody @Valid UserDTO.EmailVerificationReq req) {
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
}
