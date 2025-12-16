package com.codingshuttle.project.airBnb.service.Impl;

import com.codingshuttle.project.airBnb.Repository.InventoryRepository;
import com.codingshuttle.project.airBnb.entity.Inventory;
import com.codingshuttle.project.airBnb.entity.Room;
import com.codingshuttle.project.airBnb.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Override
    public void initializeRoomForAYear(Room room) {
        LocalDate today=LocalDate.now();
        LocalDate endDate= today.plusYears(1);
        List<Inventory> inventoryList = new ArrayList<>();
        for(;!today.isAfter(endDate);today=today.plusDays(1)){
            Inventory inventory=Inventory.builder()
                    .hotel(room.getHotel())
                    .room(room)
                    .bookedCount(0)
                    .city(room.getHotel().getCity())
                    .date(today)
                    .price(room.getBasePrice())
                    .surgeFactor(BigDecimal.ONE)
                    .totalCount(room.getTotalCount())
                    .closed(false)
                    .build();
            inventoryList.add(inventory);
        }
        inventoryRepository.saveAll(inventoryList);
    }

    @Override
    @Transactional
    public void deleteFutureInventories(Room room) {
        LocalDate today=LocalDate.now();
        inventoryRepository.deleteByDateAfterAndRoom(today,room);
    }
}
