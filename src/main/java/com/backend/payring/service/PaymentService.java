package com.backend.payring.service;

import com.backend.payring.dto.payment.GetPaymentDTO;
import com.backend.payring.dto.payment.PaymentCreateDTO;
import com.backend.payring.dto.transfer.CompletedUserDTO;
import com.backend.payring.dto.transfer.UnCompletedUserDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PaymentService {
    PaymentCreateDTO.Res createPayment(PaymentCreateDTO.Req req, MultipartFile image);

    GetPaymentDTO.PaymentList getPaymentList(Long roomId);

    GetPaymentDTO.PaymentDetail getPaymentDetail(Long paymentId);

    void deletePayment(Long paymentId);

    void startSettling(Long roomId);

    List<CompletedUserDTO.UserInfo> getFinishTeamMemberList(Long roomId);

    List<UnCompletedUserDTO.SenderInfo> getUnFinishedTeamMemberList(Long roomId);
}
