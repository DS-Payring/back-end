package com.backend.payring.controller;

import com.backend.payring.code.ResponseCode;
import com.backend.payring.dto.response.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health-check")
public class HealthCheckController {

    @GetMapping
    public ResponseEntity<String> heathCheck() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("healthy");
    }
}
