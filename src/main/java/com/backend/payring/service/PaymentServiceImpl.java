package com.backend.payring.service;

import com.backend.payring.code.ErrorCode;
import com.backend.payring.converter.PaymentConverter;
import com.backend.payring.dto.payment.GetPaymentDTO;
import com.backend.payring.dto.payment.PaymentCreateDTO;
import com.backend.payring.entity.PaymentEntity;
import com.backend.payring.entity.RoomEntity;
import com.backend.payring.entity.UserEntity;
import com.backend.payring.exception.RoomException;
import com.backend.payring.exception.UserException;
import com.backend.payring.repository.PaymentRepository;
import com.backend.payring.repository.RoomRepository;
import com.backend.payring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final S3Uploader s3Uploader;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @Transactional
    public PaymentCreateDTO.Res createPayment(PaymentCreateDTO.Req req, MultipartFile image) {
        String url = null;

        // 이미지를 업로드했다면 s3에 업로드
        if (image != null) {
            try {
                url = s3Uploader.upload(image, "payment");
                log.info("S3 업로드 성공: {}", url);
            } catch (IOException e) {
                log.error("S3 업로드 실패: {}", image.getOriginalFilename(), e);
                throw new RuntimeException("S3 업로드 실패", e);
            }
        } else {
            log.info("업로드할 파일이 제공되지 않았습니다.");
        }

        UserEntity user = userRepository.findById(1L)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        RoomEntity room = roomRepository.findById(req.getProjectId())
                .orElseThrow(() -> new RoomException(ErrorCode.ROOM_NOT_FOUND));

        PaymentEntity payment = PaymentConverter.toPaymentEntity(req, user, room, url);

        paymentRepository.save(payment);
        return PaymentConverter.toRes(payment);

    }

    @Override
    public GetPaymentDTO.PaymentList getPaymentList(Long roomId) {
        RoomEntity room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomException(ErrorCode.ROOM_NOT_FOUND));

        List<PaymentEntity> payments = paymentRepository.findAllByRoomOrderByIdDesc(room);

        return PaymentConverter.toPaymentList(payments);
    }
}
