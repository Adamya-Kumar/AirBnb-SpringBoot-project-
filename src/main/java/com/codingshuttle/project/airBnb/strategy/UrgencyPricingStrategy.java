package com.codingshuttle.project.airBnb.strategy;

import com.codingshuttle.project.airBnb.entity.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;


@RequiredArgsConstructor
public class UrgencyPricingStrategy implements PricingStrategy{
    private final PricingStrategy wapper;

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal price = wapper.calculatePrice(inventory);
        LocalDate today=LocalDate.now();
        if(!inventory.getDate().isBefore(today) && inventory.getDate().isAfter(today.plusDays(7))){
            price = price.multiply(BigDecimal.valueOf(1.15));
        }
        return price;
    }
}
