package com.codingshuttle.project.airBnb.strategy;

import com.codingshuttle.project.airBnb.entity.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class SugarPricingStrategy implements PricingStrategy{
    private final PricingStrategy wapper;

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        return wapper.calculatePrice(inventory).multiply(inventory.getSurgeFactor());
    }
}
