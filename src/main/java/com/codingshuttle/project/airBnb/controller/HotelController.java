package com.codingshuttle.project.airBnb.controller;

import com.codingshuttle.project.airBnb.advice.ApiResponse;
import com.codingshuttle.project.airBnb.dto.HotelDTO;
import com.codingshuttle.project.airBnb.service.HotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/hotels")
@RequiredArgsConstructor
@Slf4j
public class HotelController {
    private final HotelService hotelService;


    @PostMapping
    public ResponseEntity<ApiResponse<HotelDTO>> createNewHotel(@RequestBody HotelDTO newHotel){
        log.info("Try to created new hotel by name: {}",newHotel.getName());
       HotelDTO hotelDTO = hotelService.createNewHotel(newHotel);
        return new ResponseEntity<>(new ApiResponse<>(hotelDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{hotelId}")
    public ResponseEntity<ApiResponse<HotelDTO>> getHotelById(@PathVariable Long hotelId){
        HotelDTO hotelDTO = hotelService.getHotelById(hotelId);
       return ResponseEntity.ok(new ApiResponse<>(hotelDTO));
    }

    @PutMapping("/{hotelId}")
    public ResponseEntity<ApiResponse<HotelDTO>> updateHotelDetails(@PathVariable Long hotelId,@RequestBody HotelDTO updateHotel){
        HotelDTO hotelDTO = hotelService.updateHotelDetails(hotelId,updateHotel);
        return ResponseEntity.ok(new ApiResponse<>(hotelDTO));
    }

    @DeleteMapping("/{hotelId}")
    public ResponseEntity<ApiResponse<Boolean>> deleteHotelById(@PathVariable Long hotelId){
       Boolean isDelete= hotelService.deleteHotelById(hotelId);
        return ResponseEntity.ok(new ApiResponse<>(isDelete));
    }

    @PatchMapping("/{hotelId}")
    public ResponseEntity<ApiResponse<Boolean>> activeHotel(@PathVariable Long hotelId){
        Boolean isPassed=hotelService.activateHotel(hotelId);
        return ResponseEntity.ok(new ApiResponse<>(isPassed));
    }
}
