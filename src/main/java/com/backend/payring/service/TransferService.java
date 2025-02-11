package com.backend.payring.service;

import com.backend.payring.dto.transfer.ReceiverDTO;
import com.backend.payring.dto.transfer.VerifyTransferDTO;
import org.springframework.web.multipart.MultipartFile;

public interface TransferService {
    ReceiverDTO.TransferInfo getReceiverInfo(Long paymentId);

    VerifyTransferDTO.Res verifyTransfer(Long paymentId, MultipartFile image);
}
