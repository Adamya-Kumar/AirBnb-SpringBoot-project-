package com.codingshuttle.project.airBnb.service;

import com.codingshuttle.project.airBnb.dto.HotelDTO;
import com.codingshuttle.project.airBnb.entity.Hotel;

public interface HotelService {

    HotelDTO createNewHotel(HotelDTO newHotel);

    HotelDTO getHotelById(Long id);

    HotelDTO updateHotelDetails(Long id,HotelDTO updateHotelDetails);

    Boolean deleteHotelById(Long hotelId);

    Boolean activateHotel(Long roomId);

}
