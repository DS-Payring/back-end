package com.backend.payring.service;

import com.backend.payring.dto.transfer.ReceiverDTO;

public interface TransferService {
    ReceiverDTO.TransferInfo getReceiverInfo(Long paymentId);
}
