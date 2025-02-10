package com.backend.payring.service;

import com.backend.payring.dto.payment.PaymentCreateDTO;
import org.springframework.web.multipart.MultipartFile;

public interface PaymentService {
    PaymentCreateDTO.Res createPayment(PaymentCreateDTO.Req req, MultipartFile image);
}
