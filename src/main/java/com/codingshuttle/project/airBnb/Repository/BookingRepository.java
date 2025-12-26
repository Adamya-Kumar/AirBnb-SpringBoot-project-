package com.codingshuttle.project.airBnb.Repository;


import com.codingshuttle.project.airBnb.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking,Long> {
}
