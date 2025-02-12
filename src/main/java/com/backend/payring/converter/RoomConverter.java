package com.backend.payring.converter;

import com.backend.payring.dto.room.DurationStatusDTO;
import com.backend.payring.entity.RoomEntity;

import java.util.List;

public class RoomConverter {

    public static DurationStatusDTO.RoomInfo toRoomInfo(RoomEntity room) {
        return DurationStatusDTO.RoomInfo.builder()
                .roomId(room.getId())
                .roomName(room.getRoomName())
                .totalAmount(room.getTotalAmount())
                .roomStatus(room.getRoomStatus().name()) // Enum -> String 변환
                .roomImage(room.getRoomImage())
                .build();
    }

    public static DurationStatusDTO.DurationStatus toDurationStatus(int totalSettledAmount, int unSettledAmount, List<DurationStatusDTO.RoomInfo> roomInfoList) {
        return DurationStatusDTO.DurationStatus.builder()
                .totalSettledAmount(totalSettledAmount)
                .unSettledAmount(unSettledAmount)
                .roomInfos(roomInfoList)
                .build();
    }
}
