package com.codingshuttle.project.airBnb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelInfoDTO {
    private HotelDTO hotel;
    private List<RoomDTO> rooms;


}
