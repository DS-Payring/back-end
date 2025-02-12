package com.backend.payring.service;

import com.backend.payring.dto.room.DurationStatusDTO;

public interface RoomService {
    DurationStatusDTO.DurationStatus getDurationStatus(Integer day, Long userId);
}
