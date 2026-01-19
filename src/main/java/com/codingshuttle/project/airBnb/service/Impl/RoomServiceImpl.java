package com.codingshuttle.project.airBnb.service.Impl;

import com.codingshuttle.project.airBnb.Repository.HotelRepository;
import com.codingshuttle.project.airBnb.Repository.RoomRepository;
import com.codingshuttle.project.airBnb.dto.RoomDTO;
import com.codingshuttle.project.airBnb.entity.Hotel;
import com.codingshuttle.project.airBnb.entity.Room;
import com.codingshuttle.project.airBnb.entity.User;
import com.codingshuttle.project.airBnb.exceptions.ResourceNotFoundException;
import com.codingshuttle.project.airBnb.exceptions.UnAuthorisedException;
import com.codingshuttle.project.airBnb.service.InventoryService;
import com.codingshuttle.project.airBnb.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;
    private final InventoryService inventoryService;

    @Override
    public RoomDTO createNewRoom(Long hotelId,RoomDTO newRoom) {
        log.info("Try to create new Room by name: {}",newRoom.getHotel());
        Hotel hotel=hotelRepository.findById(hotelId).orElseThrow(()->
                new ResourceNotFoundException("Enable to fetch the hotel by id:{}"));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorisedException("This user is not owner of this hotel id: "+hotelId);
        }

        Room room=modelMapper.map(newRoom,Room.class);
        room.setHotel(hotel);
        Room savedRoom=roomRepository.save(room);
       log.info("Successfully create new Room and this id: {}",room.getId());
       if(hotel.getActive()) {
           log.info("Try add Inventory of this Room for One Year" + room.getId());
           inventoryService.initializeRoomForAYear(room);
           log.info("Success add Inventory of the One Year" + room.getId());
       }
       return modelMapper.map(savedRoom,RoomDTO.class);
    }

    @Override
    public List<RoomDTO> getAllRoomInHotel(Long hotelId){
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(()->
                new ResourceNotFoundException("Enable to fetch the hotel by id:{}"));
        return hotel.getRooms()
                .stream()
                .map((element)->modelMapper.map(element,RoomDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public RoomDTO getRoomById(Long roomId) {
        log.info("Try to Get Room by Id:{}",roomId);
        Room room=roomRepository.findById(roomId).orElseThrow(()->
                new ResourceNotFoundException("Error failed to get Room by Id:"+roomId));
        System.out.println(room);
        return modelMapper.map(room,RoomDTO.class);
    }

    @Override
    @Transactional
    public Boolean deleteRoomById(Long roomId) {
        log.info("Try delete Room by Id: "+roomId);
        Room room=roomRepository.findById(roomId).orElseThrow(()->
                new ResourceNotFoundException("Error failed to get Room by Id:"+roomId));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(room.getHotel().getOwner())){
            throw new UnAuthorisedException("This used is not owner of this hotel id: "+roomId);
        }
        inventoryService.deleteAllInventories(room);
        roomRepository.deleteById(roomId);
        log.info("Successfully delete Room by Id: {}",roomId);
        return true;
    }

}
