package com.smartparking.dto.response;

import com.smartparking.enums.SlotType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingSlotDTO {

    private Long id;
    private String slotNumber;
    private SlotType slotType;
}
