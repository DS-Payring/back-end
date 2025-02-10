package com.backend.payring.service;

import com.backend.payring.code.ErrorCode;
import com.backend.payring.converter.TempConverter;
import com.backend.payring.dto.temp.TempCreateDTO;
import com.backend.payring.entity.TempEntity;
import com.backend.payring.exception.TempException;
import com.backend.payring.repository.TempRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TempServiceImpl implements TempService{

    private final TempRepository tempRepository;

    @Transactional
    public TempCreateDTO.Res createTemp(TempCreateDTO.Req tempCreateDTO) {
        if (tempCreateDTO.getRequestMessage().equals("에러")) {
            throw new TempException(ErrorCode.TEMP_EXCEPTION);
        }

        TempEntity savedTempEntity = tempRepository.save(TempConverter.toTempEntity(tempCreateDTO));
        return TempConverter.toRes(savedTempEntity);
    }
}
