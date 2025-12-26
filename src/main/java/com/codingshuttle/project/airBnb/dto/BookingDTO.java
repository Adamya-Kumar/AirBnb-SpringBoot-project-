package com.codingshuttle.project.airBnb.dto;

import com.codingshuttle.project.airBnb.entity.Guest;
import com.codingshuttle.project.airBnb.entity.Hotel;
import com.codingshuttle.project.airBnb.entity.Room;
import com.codingshuttle.project.airBnb.entity.User;
import com.codingshuttle.project.airBnb.entity.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class BookingDTO {

    private Long id;
    private LocalDateTime createdAt;
    private Integer roomCount;
    private LocalDateTime updatedAt;
    private BookingStatus status;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Set<GuestDTO> guests;
}

