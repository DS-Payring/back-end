package com.backend.payring.service;

import com.backend.payring.code.ErrorCode;
import com.backend.payring.converter.UserConverter;
import com.backend.payring.dto.user.UpdateUserDTO;
import com.backend.payring.dto.user.UserDTO;
import com.backend.payring.entity.AccountEntity;
import com.backend.payring.entity.UserEntity;
import com.backend.payring.jwt.JWTUtil;
import com.backend.payring.repository.UserRepository;
import com.backend.payring.exception.UserException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailServiceImpl emailServiceImpl;
    private final JWTUtil jwtUtil;

    @Transactional
    public void sendVerificationCode(String email) throws MessagingException {
        Optional<UserEntity> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            UserEntity existUser = optionalUser.get();

            if (existUser.isEmailVerified()) {
                throw new UserException(ErrorCode.DUPLICATE_EMAIL);
            }
        }

        String verificationCode = emailServiceImpl.generateVerificationCode();
        emailServiceImpl.sendVerificationCode(email, verificationCode);

        // 이메일 인증용 사용자 엔티티
        UserEntity user = UserEntity.createForEmailVerification(email, verificationCode); 
        userRepository.save(user);
    }

    @Transactional
    public void verifyEmail(UserDTO.EmailVerificationReq req) {
        UserEntity user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        if (!req.getVerificationCode().equals(user.getEmailVerificationNum())) {
           throw new UserException(ErrorCode.INVALID_VERIFICATION_CODE);
        }

        user.verifyEmail();
    }

    @Transactional
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public UserDTO.Res signup(UserDTO.SignUpReq req) {
        UserEntity user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        if (!user.isEmailVerified()) {
            throw new UserException(ErrorCode.EMAIL_NOT_VERIFIED);
        }

        if(user.getUserName() != null && !user.getUserName().isEmpty()) {
            throw new UserException(ErrorCode.DUPLICATE_EMAIL);
        }

        user.updateUserInfo(
                req.getUserName(),
                passwordEncoder.encode(req.getPassword()),
                req.getProfileImage(),
                req.getPayUrl()
        );

        if (req.getAccounts() != null && !req.getAccounts().isEmpty()) {
            req.getAccounts().forEach(accountReq -> {
                AccountEntity account = UserConverter.toAccountEntity(user, accountReq);
                user.addAccount(account);
            });
        }

        String token = jwtUtil.createJwt(user.getId().toString());
        return UserConverter.toRes(user, user.getAccounts(), token);
    }

    @Transactional
    public UserDTO.Res login(UserDTO.LoginReq req) {
        UserEntity user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new UserException(ErrorCode.INVALID_PASSWORD);
        }

        String token = jwtUtil.createJwt(user.getId().toString());
        return UserConverter.toRes(user, user.getAccounts(), token);
    }

    @Transactional
    public UpdateUserDTO.Res updateUseInfo(UserEntity user, UpdateUserDTO.Req req) {
        UserEntity fullInfoUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        fullInfoUser.updateUserInfo(req.getUserName(), req.getPayUrl());

        userRepository.save(fullInfoUser);

        return UserConverter.toUpdteUserRes(fullInfoUser);
    }

    @Transactional(readOnly = true)
    public UpdateUserDTO.Res getUserInfo(UserEntity user) {
        UserEntity fullInfoUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        return UserConverter.toUpdteUserRes(fullInfoUser);
    }
}