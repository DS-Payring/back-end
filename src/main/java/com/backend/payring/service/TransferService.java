package com.backend.payring.service;

import com.backend.payring.dto.transfer.ReceiveDTO;
import com.backend.payring.dto.transfer.ReceiverDTO;
import com.backend.payring.dto.transfer.SendDTO;
import com.backend.payring.dto.transfer.VerifyTransferDTO;
import org.springframework.web.multipart.MultipartFile;

public interface TransferService {
    ReceiverDTO.TransferInfo getReceiverInfo(Long transferId);

    VerifyTransferDTO.Res verifyTransfer(Long paymentId, MultipartFile image);

    SendDTO.Sender getSenderTransferStatus(Long roomId, Long userId);

    ReceiveDTO.Receiver getReceiverTransferStatus(Long roomId, Long userId);
}
