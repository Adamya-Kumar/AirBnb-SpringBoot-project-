package com.codingshuttle.project.airBnb.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
@Data
public class BookingRequestDTO {
    private Long hotelId;
    private Long roomId;
    @JsonFormat(pattern = "yyyy-MM-dd") // Forces Jackson to handle this format
    private LocalDate checkInDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkOutDate;
    private Integer roomCount;
}
