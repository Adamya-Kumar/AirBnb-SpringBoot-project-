package com.codingshuttle.project.airBnb.controller;

import com.codingshuttle.project.airBnb.advice.ApiResponse;
import com.codingshuttle.project.airBnb.dto.BookingDTO;
import com.codingshuttle.project.airBnb.dto.BookingRequestDTO;
import com.codingshuttle.project.airBnb.dto.GuestDTO;
import com.codingshuttle.project.airBnb.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class HotelBookingController {

    private final BookingService bookingService;

    @PostMapping("/init")
    public ResponseEntity<ApiResponse<BookingDTO>> intitaliseBooking(@RequestBody BookingRequestDTO bookingRequestDTO){
        return ResponseEntity.ok(new ApiResponse<>(bookingService.initialiseBooking(bookingRequestDTO)));
    }

    @PostMapping("/{bookingId}/addGuests")
    public ResponseEntity<ApiResponse<BookingDTO>> addGuests(@PathVariable Long bookingId, @RequestBody List<GuestDTO> guestDTOList){
        return ResponseEntity.ok(new ApiResponse<>(bookingService.addGuests(bookingId,guestDTOList)));
    }
}
