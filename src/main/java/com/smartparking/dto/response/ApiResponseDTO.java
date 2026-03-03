package com.smartparking.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDTO {

    private boolean success;
    private String message;
    private Object data;
    
    public ApiResponseDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}