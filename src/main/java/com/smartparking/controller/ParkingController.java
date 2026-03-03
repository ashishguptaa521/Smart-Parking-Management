package com.smartparking.controller;

import com.smartparking.dto.request.EntryRequestDTO;
import com.smartparking.dto.request.ReservationRequestDTO;
import com.smartparking.dto.response.ApiResponseDTO;
import com.smartparking.dto.response.EntryResponseDTO;
import com.smartparking.dto.response.ExitResponseDTO;
import com.smartparking.dto.response.ParkingHistoryDTO;
import com.smartparking.dto.response.ParkingSlotDTO;
import com.smartparking.service.ParkingService;
import com.smartparking.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * REST Controller for Parking Management System APIs
 */
@RestController
@RequestMapping("/api/parking")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ParkingController {

    private final ParkingService parkingService;
    private final ReservationService reservationService;

    /**
     * Vehicle Entry API
     * POST /api/parking/entry
     * 
     * @param entryRequest - Entry request containing vehicle details
     * @return EntryResponseDTO with ticket information
     */
    @PostMapping("/entry")
    public ResponseEntity<ApiResponseDTO> vehicleEntry(
            @Valid @RequestBody EntryRequestDTO entryRequest) {
        
        EntryResponseDTO response = parkingService.vehicleEntry(entryRequest);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDTO(true, "Vehicle entry processed successfully", response));
    }

    /**
     * Vehicle Exit & Fee Calculation API
     * POST /api/parking/exit/{ticketId}
     * 
     * @param ticketId - Parking ticket ID
     * @return ExitResponseDTO with fee calculation details
     */
    @PostMapping("/exit/{ticketId}")
    public ResponseEntity<ApiResponseDTO> vehicleExit(@PathVariable Long ticketId) {
        
        ExitResponseDTO response = parkingService.vehicleExit(ticketId);
        
        return ResponseEntity.ok(
                new ApiResponseDTO(true, "Vehicle exit processed successfully. Parking fee calculated.", response));
    }

    /**
     * Get Available Parking Slots API
     * GET /api/parking/available
     * 
     * @return List of available parking slots
     */
    @GetMapping("/available")
    public ResponseEntity<ApiResponseDTO> getAvailableSlots() {
        
        List<ParkingSlotDTO> availableSlots = parkingService.getAvailableSlots();
        
        return ResponseEntity.ok(
                new ApiResponseDTO(true, "Available parking slots retrieved successfully", availableSlots));
    }

    /**
     * Reserve a Parking Slot API
     * POST /api/parking/reserve
     * 
     * @param reservationRequest - Reservation details including slot and vehicle info
     * @return Reservation confirmation details
     */
    @PostMapping("/reserve")
    public ResponseEntity<ApiResponseDTO> reserveSlot(
            @Valid @RequestBody ReservationRequestDTO reservationRequest) {
        
        ApiResponseDTO response = reservationService.reserveSlot(reservationRequest);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Cancel a Reservation API
     * DELETE /api/parking/reserve/{id}
     * 
     * @param id - Reservation/Slot ID to cancel
     * @return Cancellation confirmation
     */
    @DeleteMapping("/reserve/{id}")
    public ResponseEntity<ApiResponseDTO> cancelReservation(@PathVariable Long id) {
        
        ApiResponseDTO response = reservationService.cancelReservation(id);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Search Vehicles by License Plate API
     * GET /api/parking/search?query={licensePlate}
     * 
     * @param query - License plate search query
     * @return List of parking history matching the search query
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponseDTO> searchVehicles(@RequestParam String query) {
        
        List<ParkingHistoryDTO> results = parkingService.searchByLicensePlate(query);
        
        String message = results.isEmpty() 
            ? "No vehicles found matching the search query" 
            : "Vehicles found successfully";
        
        return ResponseEntity.ok(new ApiResponseDTO(true, message, results));
    }

    /**
     * Get Parking History for a Vehicle API
     * GET /api/parking/history/{licensePlate}
     * 
     * @param licensePlate - Vehicle license plate number
     * @param pageable - Pagination parameters (optional)
     * @return Parking history for the specified vehicle
     */
    @GetMapping("/history/{licensePlate}")
    public ResponseEntity<ApiResponseDTO> getParkingHistory(
            @PathVariable String licensePlate,
            Pageable pageable) {
        
        Page<ParkingHistoryDTO> history = parkingService.getParkingHistory(licensePlate, pageable);
        
        String message = history.isEmpty() 
            ? "No parking history found for this vehicle" 
            : "Parking history retrieved successfully";
        
        return ResponseEntity.ok(new ApiResponseDTO(true, message, history));
    }
}