package com.backend.payring.service;

import com.backend.payring.dto.payment.GetPaymentDTO;
import com.backend.payring.dto.payment.PaymentCreateDTO;
import org.springframework.web.multipart.MultipartFile;

public interface PaymentService {
    PaymentCreateDTO.Res createPayment(PaymentCreateDTO.Req req, MultipartFile image);

    GetPaymentDTO.PaymentList getPaymentList(Long roomId);

    GetPaymentDTO.PaymentDetail getPaymentDetail(Long paymentId);

    void deletePayment(Long paymentId);

    void startSettling(Long roomId);
}
