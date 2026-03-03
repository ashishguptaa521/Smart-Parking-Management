package com.smartparking.service;

import com.smartparking.dto.request.ReservationRequestDTO;
import com.smartparking.dto.response.ApiResponseDTO;

public interface ReservationService {

    ApiResponseDTO reserveSlot(ReservationRequestDTO request);

    ApiResponseDTO cancelReservation(Long slotId);

    boolean isSlotReserved(Long slotId);

    void releaseExpiredReservations();
}
