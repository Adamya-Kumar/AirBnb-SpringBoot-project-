package com.codingshuttle.project.airBnb.service.Impl;

import com.codingshuttle.project.airBnb.Repository.HotelMinPriceRepository;
import com.codingshuttle.project.airBnb.Repository.HotelRepository;
import com.codingshuttle.project.airBnb.Repository.InventoryRepository;
import com.codingshuttle.project.airBnb.entity.Hotel;
import com.codingshuttle.project.airBnb.entity.HotelMinPrice;
import com.codingshuttle.project.airBnb.entity.Inventory;
import com.codingshuttle.project.airBnb.strategy.PricingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PricingUpdateService {
    private final HotelRepository hotelRepository;
    private final InventoryRepository inventoryRepository;
    private final HotelMinPriceRepository hotelMinPriceRepository;
    private final PricingService pricingService;


    //Schedular to update the inventory and HotelMinPrice table every hour
    @Scheduled(cron = "0 0 * * * *")
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
        log.info("Update Hotel Prices for hotel id: {}",hotel.getId());
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusYears(1);

        List<Inventory> inventoryList = inventoryRepository.findByHotelAndDateBetween(hotel,startDate,endDate);

        updateInventoryPrice(inventoryList);

        updateHotelMinPrice(hotel,inventoryList,startDate,endDate);
    }

    private void updateHotelMinPrice(Hotel hotel,List<Inventory> inventoryList, LocalDate startDate, LocalDate endDate) {
        Map<LocalDate,BigDecimal> dailyMinPrice = inventoryList.stream()
                .collect(Collectors.groupingBy(
                        Inventory::getDate,
                        Collectors.mapping(Inventory::getPrice,Collectors.minBy(Comparator.naturalOrder()))
                        ))
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,e->e.getValue().orElse(BigDecimal.ZERO)));

        //prepare HotelPrice entities in bulk
        List<HotelMinPrice> hotelPrice = new ArrayList<>();
        dailyMinPrice.forEach((date,price)->{
            HotelMinPrice hotelMinPrice = hotelMinPriceRepository.findByHotelAndDate(hotel,date)
                    .orElse(new HotelMinPrice(hotel,date));
            hotelMinPrice.setPrice(price);
            hotelPrice.add(hotelMinPrice);
        });
        hotelMinPriceRepository.saveAll(hotelPrice);
    }

    private void updateInventoryPrice(List<Inventory> inventoryList) {
        inventoryList.forEach(inventory -> {
            BigDecimal dynamicPrice = pricingService.calculateDynamicPricing(inventory);
            inventory.setPrice(dynamicPrice);
        });
        inventoryRepository.saveAll(inventoryList);
    }
}
