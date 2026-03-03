package com.smartparking.service.impl;

import com.smartparking.dto.request.ReservationRequestDTO;
import com.smartparking.dto.response.ApiResponseDTO;
import com.smartparking.entity.ParkingSlot;
import com.smartparking.exception.ReservationException;
import com.smartparking.exception.SlotNotFoundException;
import com.smartparking.repository.ParkingSlotRepository;
import com.smartparking.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationServiceImpl implements ReservationService {

    private final ParkingSlotRepository slotRepository;

    @Override
    public ApiResponseDTO reserveSlot(ReservationRequestDTO request) {
        
        ParkingSlot slot = slotRepository.findById(request.getSlotId())
                .orElseThrow(() -> new SlotNotFoundException("Parking slot not found"));

        if (slot.isOccupied()) {
            throw new ReservationException("Slot is already occupied");
        }

        if (slot.getReservedUntil() != null && slot.getReservedUntil().isAfter(LocalDateTime.now())) {
            throw new ReservationException("Slot is already reserved");
        }

        // Reserve the slot for the specified duration (default 30 minutes)
        LocalDateTime reservationExpiry = LocalDateTime.now().plusMinutes(
                request.getDurationMinutes() != null ? request.getDurationMinutes() : 30);

        slot.setReservedUntil(reservationExpiry);
        slot.setOccupiedByLicensePlate(request.getLicensePlate());
        
        slotRepository.save(slot);

        return new ApiResponseDTO(true, "Parking slot reserved successfully", slot);
    }

    @Override
    public ApiResponseDTO cancelReservation(Long slotId) {
        
        ParkingSlot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new SlotNotFoundException("Parking slot not found"));

        if (slot.getReservedUntil() == null || slot.getReservedUntil().isBefore(LocalDateTime.now())) {
            throw new ReservationException("No active reservation found for this slot");
        }

        slot.setReservedUntil(null);
        slot.setOccupiedByLicensePlate(null);
        
        slotRepository.save(slot);

        return new ApiResponseDTO(true, "Reservation cancelled successfully", null);
    }

    @Override
    public boolean isSlotReserved(Long slotId) {
        
        ParkingSlot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new SlotNotFoundException("Parking slot not found"));

        return slot.getReservedUntil() != null && slot.getReservedUntil().isAfter(LocalDateTime.now());
    }

    @Override
    public void releaseExpiredReservations() {
        
        List<ParkingSlot> slots = slotRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (ParkingSlot slot : slots) {
            if (slot.getReservedUntil() != null && slot.getReservedUntil().isBefore(now) && !slot.isOccupied()) {
                slot.setReservedUntil(null);
                slot.setOccupiedByLicensePlate(null);
                slotRepository.save(slot);
            }
        }
    }
}