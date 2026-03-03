package com.smartparking.service;

import java.util.List;

import com.smartparking.entity.ParkingSlot;
import com.smartparking.enums.SlotType;

public interface SlotService {

    ParkingSlot findAvailableSlot(SlotType slotType);

    void markSlotOccupied(ParkingSlot slot, String licensePlate);

    void freeSlot(String slotNumber);

    List<ParkingSlot> getAllAvailableSlots();
}

