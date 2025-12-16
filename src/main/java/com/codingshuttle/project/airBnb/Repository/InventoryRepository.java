package com.codingshuttle.project.airBnb.Repository;

import com.codingshuttle.project.airBnb.entity.Inventory;
import com.codingshuttle.project.airBnb.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface InventoryRepository extends JpaRepository<Inventory,Long> {

    void deleteAllByRoom(Room room);

}
