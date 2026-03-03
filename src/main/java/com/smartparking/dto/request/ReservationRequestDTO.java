package com.smartparking.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationRequestDTO {

    @NotNull(message = "Slot ID is required")
    private Long slotId;

    @NotBlank(message = "License plate is required")
    private String licensePlate;

    // Duration in minutes for the reservation (optional, default 30 minutes)
    private Integer durationMinutes;
}