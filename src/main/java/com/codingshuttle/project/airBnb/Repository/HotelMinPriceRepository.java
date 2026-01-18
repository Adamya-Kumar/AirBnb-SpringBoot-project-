package com.codingshuttle.project.airBnb.Repository;

import com.codingshuttle.project.airBnb.dto.HotelPriceDTO;
import com.codingshuttle.project.airBnb.entity.Hotel;
import com.codingshuttle.project.airBnb.entity.HotelMinPrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface HotelMinPriceRepository extends JpaRepository<HotelMinPrice,Long> {

    @Query("""
    SELECT DISTINCT com.codingshuttle.project.airBnb.dto.HotelPriceDTO(i.hotel,AVG(i.price))
    FROM HotelMinPrice i
    WHERE i.hotel.city = :city
    AND i.date BETWEEN :startDate AND :endDate
    AND i.hotel.active = TRUE
    GROUP BY i.hotel
    
    """)
    Page<HotelPriceDTO> findHotelsWithAvailableInventory(
            @Param("city") String city,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("roomCount") Integer roomCount,
            @Param("dateCount") long dateCount,
            Pageable pageable
    );
}
