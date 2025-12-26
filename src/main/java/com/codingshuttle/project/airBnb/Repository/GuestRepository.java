package com.codingshuttle.project.airBnb.Repository;

import com.codingshuttle.project.airBnb.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

public interface GuestRepository extends JpaRepository<Guest, Long> {

}