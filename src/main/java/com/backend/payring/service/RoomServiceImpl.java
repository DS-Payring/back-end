package com.backend.payring.service;

import com.backend.payring.converter.RoomConverter;
import com.backend.payring.dto.room.DurationStatusDTO;
import com.backend.payring.entity.RoomEntity;
import com.backend.payring.entity.enums.RoomStatus;
import com.backend.payring.repository.RoomRepository;
import com.backend.payring.repository.TeamMemberRepository;
import com.backend.payring.repository.TransferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService{
    private final RoomRepository roomRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final TransferRepository transferRepository;

    @Override
    @Transactional(readOnly = true)
    public DurationStatusDTO.DurationStatus getDurationStatus(Integer day, Long userId) {
        // 현재 날짜로부터 N일 전
        LocalDateTime startDate = LocalDateTime.now().minusDays(day);

        // 사용자가 속한 방 ID 목록
        List<Long> userRoomIds = teamMemberRepository.findRoomIdsByUserId(userId);

        // 해당 사용자가 속한 방 중 COLLECTING 상태가 아닌, 기간 내 생성된 방 조회
        List<RoomEntity> rooms = roomRepository.findByIdInAndCreatedAtAfterAndRoomStatusNot(
                userRoomIds, startDate, RoomStatus.COLLECTING
        );

        int totalSettledAmount = 0; // 총 정산 금액
        int unSettledAmount = 0; // 아직 정산하지 않은 금액

        List<DurationStatusDTO.RoomInfo> roomInfoList = new ArrayList<>();

        for (RoomEntity room : rooms) {

            // isComplete==true인 transfer 총합 계산 (완료된 송금 금액)
            int settledAmount = transferRepository.sumCompletedTransfersByRoomId(room.getId());

            // isComplete==false인 transfer 총합 계산 (미완료된 송금 금액)
            int unsettledAmount = transferRepository.sumUncompletedTransfersByRoomId(room.getId());

            totalSettledAmount += settledAmount; // 완료된 총 금액 더함
            unSettledAmount += unsettledAmount; // 아직 송금되지 않은 총 금액 더함

            roomInfoList.add(RoomConverter.toRoomInfo(room));
        }

        return RoomConverter.toDurationStatus(totalSettledAmount, unSettledAmount, roomInfoList);
    }



}
