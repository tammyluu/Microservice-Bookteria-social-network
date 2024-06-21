package com.tammy.identityservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tammy.identityservice.dto.response.ApiResponse;
import com.tammy.identityservice.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        response.setStatus(errorCode.getStatusCode().value());
        //important for consumer understand which type of data is returned
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        //return response with body (status, code, message)
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        //convert object to String
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        //commit response
        response.flushBuffer();
    }
}
