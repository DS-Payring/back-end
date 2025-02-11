package com.backend.payring.converter;

import com.backend.payring.dto.transfer.CompletedUserDTO;
import com.backend.payring.entity.UserEntity;

import java.util.List;
import java.util.stream.Collectors;

public class UserConverter {

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
