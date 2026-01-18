package com.codingshuttle.project.airBnb.controller;

import com.codingshuttle.project.airBnb.advice.ApiResponse;
import com.codingshuttle.project.airBnb.dto.HotelDTO;
import com.codingshuttle.project.airBnb.dto.HotelInfoDTO;
import com.codingshuttle.project.airBnb.dto.HotelPriceDTO;
import com.codingshuttle.project.airBnb.dto.HotelSearchRequestDTO;
import com.codingshuttle.project.airBnb.service.HotelService;
import com.codingshuttle.project.airBnb.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelBrowserController {

    private final InventoryService inventoryService;
    private final HotelService hotelService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<HotelPriceDTO>>> searchHotel(@RequestBody HotelSearchRequestDTO searchRequestDTO){
        return ResponseEntity.ok(new ApiResponse<>(inventoryService.searchHostels(searchRequestDTO).getContent()));
    }

    @GetMapping("/{hotelId}/info")
    public ResponseEntity<ApiResponse<HotelInfoDTO>> getHotelInfo(@PathVariable Long hotelId){
        return ResponseEntity.ok(new ApiResponse<>(hotelService.getHotelInfo(hotelId)));
    }
}

