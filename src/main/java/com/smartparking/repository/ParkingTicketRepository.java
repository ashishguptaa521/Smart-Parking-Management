package com.smartparking.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.smartparking.entity.ParkingTicket;

public interface ParkingTicketRepository extends JpaRepository<ParkingTicket, Long> {

	// Parking history (paginated) - find all tickets by license plate
	Page<ParkingTicket> findByLicensePlate(String licensePlate, Pageable pageable);

	// Search by license plate (non-paginated)
	List<ParkingTicket> findByLicensePlateContaining(String licensePlate);

	// Get active parking (no exit yet)
	List<ParkingTicket> findByExitTimeIsNull();

	// Get revenue report between dates
	List<ParkingTicket> findByEntryTimeBetween(java.time.LocalDateTime start, java.time.LocalDateTime end);
}
