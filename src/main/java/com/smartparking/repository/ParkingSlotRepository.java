package com.smartparking.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartparking.entity.ParkingSlot;
import com.smartparking.enums.SlotType;

public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {

	// gets parking slot by slot number
	Optional<ParkingSlot> findBySlotNumber(String slotNumber);

	// gets all parking slots that are not occupied
	List<ParkingSlot> findByIsOccupiedFalse();

	// gets all parking slots that are not occupied and of a specific type
	List<ParkingSlot> findByIsOccupiedFalseAndSlotType(SlotType slotType);

	// counts the number of parking slots that are not occupied
	long countByIsOccupiedFalse();

	// finds all parking slots that were reserved but now free
	List<ParkingSlot> findByreservedUntilBefore(LocalDateTime now);

	// Find slot reserved but not occupied
	List<ParkingSlot> findByReservedUntilIsNotNullAndIsOccupiedFalse();
}
