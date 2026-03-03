package com.smartparking.service.impl;

import com.smartparking.dto.request.EntryRequestDTO;
import com.smartparking.dto.response.EntryResponseDTO;
import com.smartparking.dto.response.ExitResponseDTO;
import com.smartparking.dto.response.ParkingHistoryDTO;
import com.smartparking.dto.response.ParkingSlotDTO;
import com.smartparking.entity.ParkingSlot;
import com.smartparking.entity.ParkingTicket;
import com.smartparking.exception.SlotNotFoundException;
import com.smartparking.mapper.Parkingmapper;
import com.smartparking.repository.ParkingTicketRepository;
import com.smartparking.service.ParkingService;
import com.smartparking.service.PricingService;
import com.smartparking.service.SlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ParkingServiceImpl implements ParkingService {

    private final ParkingTicketRepository ticketRepository;
    private final SlotService slotService;
    private final PricingService pricingService;
    private final Parkingmapper mapper;

    @Override
    @CacheEvict(value = "availableSlots", allEntries = true)
    public EntryResponseDTO vehicleEntry(EntryRequestDTO request) {

        ParkingSlot slot = slotService.findAvailableSlot(request.getSlotType());

        ParkingTicket ticket = ParkingTicket.builder()
                .licensePlate(request.getLicensePlate())
                .entryTime(LocalDateTime.now())
                .slotNumber(slot.getSlotNumber())
                .slotType(slot.getSlotType())
                .build();

        ticketRepository.save(ticket);

        slotService.markSlotOccupied(slot, request.getLicensePlate());

        return mapper.toEntryResponse(ticket);
    }

    @Override
    @CacheEvict(value = "availableSlots", allEntries = true)
    public ExitResponseDTO vehicleExit(Long ticketId) {

        ParkingTicket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new SlotNotFoundException("Ticket not found"));

        LocalDateTime exitTime = LocalDateTime.now();

        BigDecimal fee = pricingService.calculateFee(
                ticket.getEntryTime(),
                exitTime,
                ticket.getSlotType()
        );

        ticket.setExitTime(exitTime);
        ticket.setFeeCharged(fee);

        ticketRepository.save(ticket);

        slotService.freeSlot(ticket.getSlotNumber());

        return ExitResponseDTO.builder()
                .ticketId(ticket.getId())
                .exitTime(exitTime)
                .fee(fee)
                .build();
    }

    @Override
    public Page<ParkingHistoryDTO> getParkingHistory(String licensePlate, Pageable pageable) {
        Page<ParkingTicket> tickets = ticketRepository.findByLicensePlate(licensePlate, pageable);
        return tickets.map(mapper::toParkingHistoryDTO);
    }

    @Override
    @Cacheable(value = "availableSlots")
    public List<ParkingSlotDTO> getAvailableSlots() {
        List<ParkingSlot> availableSlots = slotService.getAllAvailableSlots();
        return availableSlots.stream()
                .map(mapper::toParkingSlotDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ParkingHistoryDTO> searchByLicensePlate(String query) {
        List<ParkingTicket> tickets = ticketRepository.findByLicensePlateContaining(query);
        return tickets.stream()
                .map(mapper::toParkingHistoryDTO)
                .collect(Collectors.toList());
    }
}
