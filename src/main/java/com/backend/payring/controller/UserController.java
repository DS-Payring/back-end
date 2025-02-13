package com.backend.payring.controller;

import com.backend.payring.code.ErrorCode;
import com.backend.payring.code.ResponseCode;
import com.backend.payring.dto.response.ResponseDTO;
import com.backend.payring.dto.user.UpdateUserDTO;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userServiceImpl;
    private final UserRepository userRepository;

    @Operation(
            summary = "유저 정보 조회 API",
            description = "마이페이지에 보일 유저 정보를 조회합니다."
    )
    @GetMapping()
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal UserEntity user) {

        if (user == null) {
            throw new UserException(ErrorCode.USER_NOT_FOUND);
        }

        UpdateUserDTO.Res res = userServiceImpl.getUserInfo(user);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_USERINFO.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_USERINFO, res));
    }

    @Operation(
            summary = "유저 정보 수정 API",
            description = "userName, payUrl 유저 정보를 수정합니다."
    )
    @PatchMapping()
    public ResponseEntity<?> updateUseInfo(
            @AuthenticationPrincipal UserEntity user,
            @RequestBody UpdateUserDTO.Req req
    ) {

        if (user == null) {
            throw new UserException(ErrorCode.USER_NOT_FOUND);
        }

        UpdateUserDTO.Res res = userServiceImpl.updateUseInfo(user, req);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_UPDATE_USERINFO.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_UPDATE_USERINFO, res));
    }

    @Operation(
            summary = "(테스트용) 유저id 확인 API",
            description = "유저아이디가 떠야 합니다."
    )
    @PatchMapping("/profile")
    public String getUserProfile(@AuthenticationPrincipal UserEntity user) {
        if (user == null) {
            throw new UserException(ErrorCode.USER_NOT_FOUND);
        }
        return "유저: " + user.getId();
    }
}
