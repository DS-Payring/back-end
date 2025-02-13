package com.backend.payring.service;

import com.backend.payring.dto.transfer.ReceiveDTO;
import com.backend.payring.dto.transfer.ReceiverDTO;
import com.backend.payring.dto.transfer.SendDTO;
import com.backend.payring.dto.transfer.VerifyTransferDTO;
import com.backend.payring.entity.UserEntity;
import org.springframework.web.multipart.MultipartFile;

public interface TransferService {
    ReceiverDTO.TransferInfo getReceiverInfo(Long transferId);

    VerifyTransferDTO.Res verifyTransfer(Long paymentId, UserEntity user, MultipartFile image);

    SendDTO.Sender getSenderTransferStatus(Long roomId, UserEntity user);

    ReceiveDTO.Receiver getReceiverTransferStatus(Long roomId, UserEntity user);
}
