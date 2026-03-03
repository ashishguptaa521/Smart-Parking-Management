package com.smartparking.service.impl;

import com.smartparking.enums.SlotType;
import com.smartparking.service.PricingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PricingServiceImpl implements PricingService {

    @Override
    public BigDecimal calculateFee(
            LocalDateTime entryTime,
            LocalDateTime exitTime,
            SlotType slotType) {

        Duration duration = Duration.between(entryTime, exitTime);
        long hours = Math.max(1, duration.toHours());

        BigDecimal rate = switch (slotType) {
            case COMPACT -> BigDecimal.valueOf(20);
            case REGULAR -> BigDecimal.valueOf(30);
            case LARGE -> BigDecimal.valueOf(50);
            case ELECTRIC -> BigDecimal.valueOf(40);
        };

        return rate.multiply(BigDecimal.valueOf(hours));
    }
}
