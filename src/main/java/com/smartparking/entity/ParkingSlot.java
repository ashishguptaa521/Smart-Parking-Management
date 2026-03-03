package com.smartparking.entity;

import com.smartparking.enums.SlotType;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "parking_slots",
       indexes = {
           @Index(name = "idx_slot_number", columnList = "slotNumber"),
           @Index(name = "idx_slot_type", columnList = "slotType")
       })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String slotNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SlotType slotType;

    @Column(nullable = false)
    private boolean isOccupied;

    @Column
    private String occupiedByLicensePlate;

    @Column
    private LocalDateTime occupiedAt;

    @Column
    private LocalDateTime reservedUntil;
}

