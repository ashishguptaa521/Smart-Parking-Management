package com.smartparking.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExitResponseDTO {

    private Long ticketId;
    private LocalDateTime exitTime;
    private BigDecimal fee;
}
