package com.backend.payring.controller;

import com.backend.payring.code.ResponseCode;
import com.backend.payring.dto.response.ResponseDTO;
import com.backend.payring.dto.temp.TempCreateDTO;
import com.backend.payring.service.TempService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/temp")
public class TempController {

    private final TempService tempService;

    @Operation(
            summary = "예시 API 제목",
            description = "예시 API 상세 설명"
    )
    @PostMapping
    public ResponseEntity<ResponseDTO<?>> singUpLocal(@RequestBody @Valid TempCreateDTO.Req req) {

        TempCreateDTO.Res res = tempService.createTemp(req);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_CREATE_TEMP.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_CREATE_TEMP, res));
    }
}
