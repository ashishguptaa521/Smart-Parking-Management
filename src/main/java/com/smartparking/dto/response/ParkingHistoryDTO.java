package com.smartparking.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingHistoryDTO {

    private Long ticketId;
    private String licensePlate;
    private String slotNumber;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private BigDecimal feeCharged;
}
