package com.smartparking.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.smartparking.enums.SlotType;

public interface PricingService {

    BigDecimal calculateFee(
            LocalDateTime entryTime,
            LocalDateTime exitTime,
            SlotType slotType);
}
