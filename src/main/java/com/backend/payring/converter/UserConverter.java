package com.backend.payring.converter;

import com.backend.payring.dto.transfer.CompleteUserDTO;
import com.backend.payring.entity.UserEntity;

import java.util.List;
import java.util.stream.Collectors;

public class UserConverter {

    public static CompleteUserDTO.UserInfo toUserInfo(UserEntity user) {
        return CompleteUserDTO.UserInfo.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .profileImage(user.getProfileImage())
                .build();
    }

    public static List<CompleteUserDTO.UserInfo> toUserInfoList(List<UserEntity> users) {
        return users.stream()
                .map(UserConverter::toUserInfo)
                .collect(Collectors.toList());
    }

}
