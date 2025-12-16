package com.codingshuttle.project.airBnb.dto;


import com.codingshuttle.project.airBnb.entity.Hotel;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class RoomDTO {
    private Long id;
    private Hotel hotel;
    private String type;
    private BigDecimal basePrice;
    private String[] photos;
    private String[] amenities;
    private Integer totalCount;
    private Integer capacity;

}
