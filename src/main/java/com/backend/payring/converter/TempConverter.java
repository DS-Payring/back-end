package com.backend.payring.converter;

import com.backend.payring.dto.temp.TempCreateDTO;
import com.backend.payring.entity.TempEntity;

public class TempConverter {
    public static TempEntity toTempEntity (TempCreateDTO.Req req) {
        return TempEntity.builder()
                .message(req.getRequestMessage())
                .build();
    }

    public static TempCreateDTO.Res toRes (TempEntity temp) {
        return TempCreateDTO.Res.builder()
                .responseMessage(temp.getMessage())
                .build();
    }
}
