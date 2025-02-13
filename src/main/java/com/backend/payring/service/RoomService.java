package com.backend.payring.service;

import com.backend.payring.dto.room.DurationStatusDTO;
import com.backend.payring.entity.UserEntity;

public interface RoomService {
    DurationStatusDTO.DurationStatus getDurationStatus(Integer day, UserEntity user);
}
