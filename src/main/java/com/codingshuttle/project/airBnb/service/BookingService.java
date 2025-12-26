package com.codingshuttle.project.airBnb.service;

import com.codingshuttle.project.airBnb.dto.BookingDTO;
import com.codingshuttle.project.airBnb.dto.BookingRequestDTO;
import com.codingshuttle.project.airBnb.dto.GuestDTO;

import java.util.List;

public interface BookingService {
    BookingDTO initialiseBooking(BookingRequestDTO bookingRequest);

    BookingDTO addGuests(Long bookingId, List<GuestDTO> guestDTOList);
}
