package com.codingshuttle.project.airBnb.service;

import com.codingshuttle.project.airBnb.dto.HotelDTO;
import com.codingshuttle.project.airBnb.dto.HotelSearchRequestDTO;
import com.codingshuttle.project.airBnb.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

public interface InventoryService {
    void initializeRoomForAYear(Room room);
    @Transactional
    void deleteAllInventories(Room room);

    Page<HotelDTO> searchHostels(HotelSearchRequestDTO searchRequestDTO);
}
