package com.codingshuttle.project.airBnb.strategy;

import com.codingshuttle.project.airBnb.entity.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@RequiredArgsConstructor
public class OccupancyPricingStrategy implements PricingStrategy{
        private final PricingStrategy wapper;

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal price = wapper.calculatePrice(inventory);
        double occupancyRate = (double) inventory.getBookedCount() / inventory.getTotalCount();
        if(occupancyRate > 0.8){
            price = price.multiply(BigDecimal.valueOf(1.2));
        }
        return price;
    }
}
