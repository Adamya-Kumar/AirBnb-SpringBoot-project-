package com.codingshuttle.project.airBnb.controller;

import com.codingshuttle.project.airBnb.advice.ApiResponse;
import com.codingshuttle.project.airBnb.dto.HotelDTO;
import com.codingshuttle.project.airBnb.dto.RoomDTO;
import com.codingshuttle.project.airBnb.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/hotels/{hotelId}/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<ApiResponse<RoomDTO>> createRoom(@PathVariable Long hotelId, @RequestBody RoomDTO newRoom){
       RoomDTO roomDTO =roomService.createNewRoom(hotelId,newRoom);
        return new ResponseEntity<>(new ApiResponse<>(roomDTO),HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RoomDTO>>> getAllRoom(@PathVariable Long hotelId){
        List<RoomDTO> roomDTO =roomService.getAllRoomInHotel(hotelId);
        return ResponseEntity.ok(new ApiResponse<>(roomDTO));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<ApiResponse<RoomDTO>> getRoomById(@PathVariable Long roomId){
       RoomDTO roomDTO = roomService.getRoomById(roomId);
        return ResponseEntity.ok(new ApiResponse<>(roomDTO));
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<ApiResponse<Boolean>> deleteById(@PathVariable Long roomId){
       Boolean isDeleted=roomService.deleteRoomById(roomId);
        return new ResponseEntity<>(new ApiResponse<>(isDeleted),HttpStatus.ACCEPTED);
    }
}
