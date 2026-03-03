package com.smartparking.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.smartparking.dto.request.EntryRequestDTO;
import com.smartparking.dto.response.EntryResponseDTO;
import com.smartparking.dto.response.ExitResponseDTO;
import com.smartparking.dto.response.ParkingHistoryDTO;
import com.smartparking.dto.response.ParkingSlotDTO;

public interface ParkingService {

    EntryResponseDTO vehicleEntry(EntryRequestDTO request);

    ExitResponseDTO vehicleExit(Long ticketId);

    Page<ParkingHistoryDTO> getParkingHistory(
            String licensePlate,
            Pageable pageable);

    List<ParkingSlotDTO> getAvailableSlots();

    List<ParkingHistoryDTO> searchByLicensePlate(String query);
}
