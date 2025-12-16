package com.codingshuttle.project.airBnb.service;

import com.codingshuttle.project.airBnb.entity.Room;
import org.springframework.transaction.annotation.Transactional;

public interface InventoryService {
    void initializeRoomForAYear(Room room);
    @Transactional
    void deleteFutureInventories(Room room);
}
