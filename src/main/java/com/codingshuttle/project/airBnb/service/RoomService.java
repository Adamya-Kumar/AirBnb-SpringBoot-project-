package com.codingshuttle.project.airBnb.service;

import com.codingshuttle.project.airBnb.dto.RoomDTO;

import java.util.List;

public interface RoomService {
    RoomDTO createNewRoom(Long hotelId,RoomDTO newRoom);
    List<RoomDTO> getAllRoomInHotel(Long hotelId);
    RoomDTO getRoomById(Long roomId);
    Boolean deleteRoomById(Long roomId);
}
