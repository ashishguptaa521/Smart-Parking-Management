package com.smartparking.entity;

import com.smartparking.enums.SlotType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "parking_tickets", indexes = { @Index(name = "idx_license_plate", columnList = "licensePlate"),
		@Index(name = "idx_entry_time", columnList = "entryTime") })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingTicket {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Column(nullable = false)
	private String licensePlate;

	@Column(nullable = false)
	private LocalDateTime entryTime;

	@Column
	private LocalDateTime exitTime;

	@Column(precision = 10, scale = 2)
	private BigDecimal feeCharged;

	@Column(nullable = false)
	private String slotNumber;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private SlotType slotType;
}
