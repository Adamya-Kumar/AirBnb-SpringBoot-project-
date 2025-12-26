package com.codingshuttle.project.airBnb.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class HotelSearchRequestDTO {
    private String city;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer roomCount;

    private Integer page=0;
    private Integer size=10;
}
