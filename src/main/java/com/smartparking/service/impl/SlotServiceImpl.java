package com.smartparking.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.smartparking.entity.ParkingSlot;
import com.smartparking.enums.SlotType;
import com.smartparking.exception.ParkingFullException;
import com.smartparking.exception.SlotNotFoundException;
import com.smartparking.repository.ParkingSlotRepository;
import com.smartparking.service.SlotService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SlotServiceImpl implements SlotService {

    private final ParkingSlotRepository slotRepository;

    @Override
    public ParkingSlot findAvailableSlot(SlotType slotType) {
        return slotRepository.findByIsOccupiedFalseAndSlotType(slotType).stream().findFirst()
                .orElseThrow(() -> new ParkingFullException("No parking slot available."));
    }

    @Override
    public void markSlotOccupied(ParkingSlot slot, String licensePlate) {
        slot.setOccupied(true);
        slot.setOccupiedByLicensePlate(licensePlate);
        slot.setOccupiedAt(LocalDateTime.now());
        slotRepository.save(slot);
    }

    @Override
    public void freeSlot(String slotNumber) {
        ParkingSlot slot = slotRepository
                .findBySlotNumber(slotNumber)
                .orElseThrow(() -> new SlotNotFoundException("Slot not found"));

        slot.setOccupied(false);
        slot.setOccupiedByLicensePlate(null);
        slot.setOccupiedAt(null);

        slotRepository.save(slot);

    }

    @Override
    public List<ParkingSlot> getAllAvailableSlots() {
        return slotRepository.findByIsOccupiedFalse();
    }

}
