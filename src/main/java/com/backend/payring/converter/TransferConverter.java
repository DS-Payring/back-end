package com.backend.payring.converter;

import com.amazonaws.services.s3.transfer.Transfer;
import com.backend.payring.dto.transfer.ReceiverDTO;
import com.backend.payring.dto.transfer.UnCompletedUserDTO;
import com.backend.payring.dto.transfer.VerifyTransferDTO;
import com.backend.payring.entity.AccountEntity;
import com.backend.payring.entity.RoomEntity;
import com.backend.payring.entity.TransferEntity;
import com.backend.payring.entity.UserEntity;

import java.util.ArrayList;
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

    public static TransferEntity toTransfer(RoomEntity room, UserEntity sender, UserEntity receiver, Integer transferAmount) {
        return TransferEntity.builder()
                .room(room)
                .sender(sender)
                .receiver(receiver)
                .amount(transferAmount)
                .isComplete(false) // 처음 생성되면 송금 완료되지 않음
                .transferImage(null) // 송금이 완료되지 않았으므로 이미지 null
                .build();
    }


    public static VerifyTransferDTO.Res toVerifyRes(TransferEntity transfer) {
        return VerifyTransferDTO.Res.builder()
                .amount(transfer.getAmount())
                .transferImage(transfer.getTransferImage())
                .isComplete(transfer.getIsComplete())
                .build();
    }

    public static UnCompletedUserDTO.SenderInfo toSenderInfo(UserEntity sender) {
        return UnCompletedUserDTO.SenderInfo.builder()
                .userId(sender.getId())
                .userName(sender.getUserName())
                .profileImage(sender.getProfileImage())
                .totalLeftAmount(0) // 일단 0으로 초기화
                .receiverInfos(new ArrayList<>()) // 빈 리스트로 초기화
                .build();
    }

    public static UnCompletedUserDTO.ReceiverInfo toReceiverInfo(Long receiverId, TransferEntity transfer, Integer amount) {
        return UnCompletedUserDTO.ReceiverInfo.builder()
                .receiverId(receiverId)
                .receiverName(transfer.getReceiver().getUserName())
                .amount(amount)
                .build();
    }

}
