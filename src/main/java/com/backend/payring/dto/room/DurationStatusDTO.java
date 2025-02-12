package com.backend.payring.dto.room;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class DurationStatusDTO {

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RoomInfo {
        private Long roomId;
        private String roomName;
        private Integer totalAmount;
        private String roomStatus;
        private String roomImage;
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class DurationStatus {
        // 총 정산 금액
        private Integer totalSettledAmount;
        // 미정산 금액
        private Integer unSettledAmount;
        // 기간에 해당하는 방 정보
        private List<RoomInfo> roomInfos;
    }
}
