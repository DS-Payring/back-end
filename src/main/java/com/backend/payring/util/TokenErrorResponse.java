package com.backend.payring.util;


import com.backend.payring.code.ErrorCode;
import com.backend.payring.dto.response.ErrorResponseDTO;
import com.backend.payring.exception.GlobalException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.PrintWriter;

public class TokenErrorResponse {
    public static void sendErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ErrorResponseDTO errorResponse = errorCode.getReasonHttpStatus();
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.setStatus(HttpStatus.BAD_REQUEST.value());

        PrintWriter writer = response.getWriter();
        writer.print(jsonResponse);
        writer.flush();
        writer.close();
    }
}
