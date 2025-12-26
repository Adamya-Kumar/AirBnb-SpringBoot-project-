package com.codingshuttle.project.airBnb.service.Impl;

import com.codingshuttle.project.airBnb.Repository.InventoryRepository;
import com.codingshuttle.project.airBnb.dto.HotelDTO;
import com.codingshuttle.project.airBnb.dto.HotelSearchRequestDTO;
import com.codingshuttle.project.airBnb.entity.Hotel;
import com.codingshuttle.project.airBnb.entity.Inventory;
import com.codingshuttle.project.airBnb.entity.Room;
import com.codingshuttle.project.airBnb.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public void initializeRoomForAYear(Room room) {
        log.info("Make Inventory from one Year Room id:{}",room.getId());
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

    @Transactional
    public void deleteAllInventories(Room room) {
        log.info("Deleting All Inteventories from room id: {}",room.getId());
        inventoryRepository.deleteAllByRoom(room);
    }

    @Override
    public Page<HotelDTO> searchHostels(HotelSearchRequestDTO searchRequestDTO) {

        log.info("Searching hotel for {} city, from {} to {}",
                searchRequestDTO.getCity(),searchRequestDTO.getStartDate(),searchRequestDTO.getEndDate());

        org.springframework.data.domain.Pageable pageable=  PageRequest.of(searchRequestDTO.getPage(),searchRequestDTO.getSize());

        if (searchRequestDTO.getStartDate() == null || searchRequestDTO.getEndDate() == null) {
            throw new IllegalArgumentException("Start date and End date are required");
        }
        long total= ChronoUnit
                .DAYS.between(searchRequestDTO.getStartDate(),searchRequestDTO.getEndDate())+1;

        Page<Hotel> hotelPage=inventoryRepository.findHotelsWithAvailableInventory(searchRequestDTO.getCity()
                ,searchRequestDTO.getStartDate(),
                searchRequestDTO.getEndDate(),
                searchRequestDTO.getRoomCount(),
                total,
                pageable
        );
        return hotelPage.map((element)->modelMapper.map(element,HotelDTO.class));
    }
}
