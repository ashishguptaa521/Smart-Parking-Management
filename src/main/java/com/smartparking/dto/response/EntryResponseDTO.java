package com.smartparking.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntryResponseDTO {

    private Long ticketId;
    private String licensePlate;
    private String slotNumber;
    private LocalDateTime entryTime;
}
