package com.backend.payring.converter;

import com.backend.payring.dto.transfer.ReceiverDTO;
import com.backend.payring.entity.AccountEntity;
import com.backend.payring.entity.RoomEntity;
import com.backend.payring.entity.UserEntity;

import java.util.List;
import java.util.stream.Collectors;

public class TransferConverter {

    public static ReceiverDTO.Account toAccount(AccountEntity account) {
        return ReceiverDTO.Account.builder()
                .accountId(account.getId())
                .bankName(account.getBankName())
                .accountNo(account.getAccountNo())
                .receiver(account.getReceiver())
                .build();
    }

    public static List<ReceiverDTO.Account> toAccountList(List<AccountEntity> accounts) {
        return accounts.stream()
                .map(TransferConverter::toAccount)
                .collect(Collectors.toList());
    }

    public static ReceiverDTO.TransferInfo toTransferInfo(RoomEntity room, UserEntity user, List<AccountEntity> accounts) {
        return ReceiverDTO.TransferInfo.builder()
                .roomId(room.getId())
                .roomName(room.getRoomName())
                .userId(user.getId())
                .username(user.getUserName())
                .payUrl(user.getPayUrl())
                .accounts(toAccountList(accounts))
                .build();
    }


}
