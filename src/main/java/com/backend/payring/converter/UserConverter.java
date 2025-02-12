package com.backend.payring.converter;

import com.backend.payring.dto.transfer.CompletedUserDTO;
import com.backend.payring.dto.user.UserDTO;
import com.backend.payring.entity.AccountEntity;
import com.backend.payring.entity.UserEntity;

import java.util.List;
import java.util.stream.Collectors;

public class UserConverter {
    public static AccountEntity toAccountEntity(UserEntity user, UserDTO.AccountReq req) {
        return AccountEntity.builder()
                .user(user)
                .bankName(req.getBankName())
                .accountNo(req.getAccountNo())
                .receiver(req.getReceiver())
                .build();
    }

    public static UserDTO.AccountRes toAccountRes(AccountEntity account) {
        return UserDTO.AccountRes.builder()
                .accountId(account.getId())
                .bankName(account.getBankName())
                .accountNo(account.getAccountNo())
                .receiver(account.getReceiver())
                .build();
    }

    public static UserDTO.Res toRes(UserEntity user, List<AccountEntity> accounts, String token) {
        return UserDTO.Res.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getUserName())
                .profileImage(user.getProfileImage())
                .payUrl(user.getPayUrl())
                .accounts(accounts.stream()
                        .map(UserConverter::toAccountRes)
                        .collect(Collectors.toList()))
                .token(token)
                .build();
    }

    public static CompletedUserDTO.UserInfo toUserInfo(UserEntity user) {
        return CompletedUserDTO.UserInfo.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .profileImage(user.getProfileImage())
                .build();
    }

    public static List<CompletedUserDTO.UserInfo> toUserInfoList(List<UserEntity> users) {
        return users.stream()
                .map(UserConverter::toUserInfo)
                .collect(Collectors.toList());
    }

}
