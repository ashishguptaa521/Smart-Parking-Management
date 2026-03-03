package com.smartparking.dto.request;

import com.smartparking.enums.SlotType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntryRequestDTO {

    @NotBlank(message = "License plate is required")
    private String licensePlate;

    @NotNull(message = "Slot type is required")
    private SlotType slotType;
}
