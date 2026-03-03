package com.smartparking.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartparking.dto.response.ApiResponseDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * JWT Authentication Entry Point
 * Handles authentication errors and returns proper JSON response
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, 
                        HttpServletResponse response,
                        AuthenticationException authException) throws IOException, ServletException {
        
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ApiResponseDTO errorResponse = new ApiResponseDTO(
                false,
                "Unauthorized: " + authException.getMessage(),
                null
        );

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), errorResponse);
    }
}