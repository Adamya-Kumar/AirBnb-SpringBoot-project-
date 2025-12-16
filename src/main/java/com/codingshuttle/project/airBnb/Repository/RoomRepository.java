package com.codingshuttle.project.airBnb.Repository;

import com.codingshuttle.project.airBnb.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room,Long> {
}
