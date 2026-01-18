package com.codingshuttle.project.airBnb.service.Impl;

import com.codingshuttle.project.airBnb.Repository.HotelMinPriceRepository;
import com.codingshuttle.project.airBnb.Repository.HotelRepository;
import com.codingshuttle.project.airBnb.Repository.InventoryRepository;
import com.codingshuttle.project.airBnb.entity.Hotel;
import com.codingshuttle.project.airBnb.entity.Inventory;
import com.codingshuttle.project.airBnb.strategy.PricingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PricingUpdateService {
    private final HotelRepository hotelRepository;
    private final InventoryRepository inventoryRepository;
    private final HotelMinPriceRepository hotelMinPriceRepository;
    private final PricingService pricingService;


    //Schedular to update the inventory and HotelMinPrice table every hour
    public void updatePrices(){
        int page=0;
        int batchSize =100;

        while (true){
            Page<Hotel> hotelPage = hotelRepository.findAll(PageRequest.of(page,batchSize));
            if(hotelPage.isEmpty()){
                break;
            }
            hotelPage.getContent().forEach(hotel -> updateHotelPrice(hotel));

            page++;
        }
    }

    private void updateHotelPrice(Hotel hotel) {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusYears(1);

        List<Inventory> inventoryList = inventoryRepository.findByHotelAndDateBetween(hotel,startDate,endDate);

        updateInventoryPrice(inventoryList);

        updateHotelMinPrice(hotel,startDate,endDate);
    }

    private void updateHotelMinPrice(Hotel hotel, LocalDate startDate, LocalDate endDate) {
       // TODO
    }

    private void updateInventoryPrice(List<Inventory> inventoryList) {
        inventoryList.forEach(inventory -> {
            BigDecimal dynamicPrice = pricingService.calculateDynamicPricing(inventory);
            inventory.setPrice(dynamicPrice);
        });
        inventoryRepository.saveAll(inventoryList);
    }
}
