package com.backend.payring.service;

import com.backend.payring.dto.temp.TempCreateDTO;

public interface TempService {
    TempCreateDTO.Res createTemp(TempCreateDTO.Req tempCreateDTO);
}
