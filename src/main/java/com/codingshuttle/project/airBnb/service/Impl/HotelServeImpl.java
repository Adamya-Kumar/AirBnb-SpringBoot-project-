package com.codingshuttle.project.airBnb.service.Impl;

import com.codingshuttle.project.airBnb.Repository.HotelRepository;
import com.codingshuttle.project.airBnb.dto.HotelDTO;
import com.codingshuttle.project.airBnb.dto.HotelInfoDTO;
import com.codingshuttle.project.airBnb.dto.RoomDTO;
import com.codingshuttle.project.airBnb.entity.Hotel;
import com.codingshuttle.project.airBnb.entity.Room;
import com.codingshuttle.project.airBnb.exceptions.ResourceNotFoundException;
import com.codingshuttle.project.airBnb.service.HotelService;
import com.codingshuttle.project.airBnb.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServeImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;
    private final InventoryService inventoryService;
    private final RoomServiceImpl roomService;

    @Override
    public HotelDTO createNewHotel(HotelDTO newHotel) {
        log.info("create this is hotel by name:{}",newHotel.getName());
         Hotel newHotelEntity =modelMapper.map(newHotel,Hotel.class);
         newHotelEntity.setActive(false);
         log.info("Created a new hotel with id: {}",newHotelEntity.getId());
       return modelMapper.map(hotelRepository.save(newHotelEntity),HotelDTO.class);
    }

    @Override
    public HotelDTO getHotelById(Long id) {
        log.info("Get Hotel By id: {}",id);
        Hotel hotel=hotelRepository
                .findById(id)
                .orElseThrow(()->{
                    log.error("Error:Resource not found by this hotel id: {}",id);
                  return  new ResourceNotFoundException("Hotel not found by id: {}");
                });
        return modelMapper.map(hotel,HotelDTO.class);
    }

    @Override
    @Transactional
    public HotelDTO updateHotelDetails(Long id, HotelDTO updateHotelDetails) {
        log.info("Update Hotel details by id: {}",id);
        Hotel hotel=hotelRepository.findById(id).orElseThrow(()->{
            log.error("Error unable to update the hotel by id: {}",id);
            throw new ResourceNotFoundException("Error unable to update the hotel by id: {}");
        });
        modelMapper.map(updateHotelDetails, hotel);
        hotel.setId(id);
        hotel.setActive(false);
        hotelRepository.save(hotel);
        log.info("Success Update Hotel details by id: {}",id);
       return modelMapper.map(hotelRepository.save(hotel),HotelDTO.class);
    }

    @Override
    @Transactional
    public Boolean deleteHotelById(Long id) {
        Hotel hotel=hotelRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Hotel not found by id:"+id));
        log.info("Delete Hotel details by id: {}",id);
        hotelRepository.deleteById(id);
        //now we have delete inventory of this rooms present in hotel of future
        for(Room room:hotel.getRooms()){
            inventoryService.deleteAllInventories(room);
            roomService.deleteRoomById(room.getId());
        }
        return true;
    }

    @Override
    @Transactional
    public Boolean activateHotel(Long hotelId) {
        Hotel hotel=hotelRepository.findById(hotelId).orElseThrow(()->{
            log.error("Error unable to update the hotel by id: "+hotelId);
            throw new ResourceNotFoundException("Error unable to update the hotel by id: "+hotelId);
        });
        hotel.setActive(true);
        //registration of new hotel and there room
        for(Room room:hotel.getRooms()){
            inventoryService.initializeRoomForAYear(room);
        }
        return true;
    }

    @Override
    public HotelInfoDTO getHotelInfo(Long hotelId) {
        Hotel hotel=hotelRepository.findById(hotelId).orElseThrow(()->{
            log.error("Error unable to update the hotel by id: "+hotelId);
            throw new ResourceNotFoundException("Error unable to update the hotel by id: "+hotelId);
        });
        List<RoomDTO> rooms=hotel.getRooms()
                .stream()
                .map((element)->modelMapper.map(element, RoomDTO.class))
                .toList();

        return new HotelInfoDTO(modelMapper.map(hotel,HotelDTO.class),rooms);
    }

}
