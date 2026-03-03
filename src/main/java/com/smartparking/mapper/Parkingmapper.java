package com.smartparking.mapper;

import com.smartparking.dto.response.EntryResponseDTO;
import com.smartparking.dto.response.ParkingHistoryDTO;
import com.smartparking.dto.response.ParkingSlotDTO;
import com.smartparking.entity.ParkingSlot;
import com.smartparking.entity.ParkingTicket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface Parkingmapper {

    @Mapping(source = "id", target = "ticketId")
    EntryResponseDTO toEntryResponse(ParkingTicket ticket);

    @Mapping(source = "id", target = "ticketId")
    ParkingHistoryDTO toParkingHistoryDTO(ParkingTicket ticket);

    ParkingSlotDTO toParkingSlotDTO(ParkingSlot slot);
}
