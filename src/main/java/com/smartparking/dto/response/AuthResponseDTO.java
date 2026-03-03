package com.smartparking.dto.response;

import lombok.*;

/**
 * Response DTO for authentication operations
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDTO {
    
    private String token;
    
    @Builder.Default
    private String type = "Bearer";
    
    private String username;
    private String role;
    
    public AuthResponseDTO(String token, String username, String role) {
        this.token = token;
        this.type = "Bearer";
        this.username = username;
        this.role = role;
    }
}
