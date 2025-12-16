package com.codingshuttle.project.airBnb.Repository;

import com.codingshuttle.project.airBnb.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel,Long> {

}
