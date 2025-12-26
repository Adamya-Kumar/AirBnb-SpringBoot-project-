package com.codingshuttle.project.airBnb.service.Impl;

import com.codingshuttle.project.airBnb.Repository.*;
import com.codingshuttle.project.airBnb.dto.BookingDTO;
import com.codingshuttle.project.airBnb.dto.BookingRequestDTO;
import com.codingshuttle.project.airBnb.dto.GuestDTO;
import com.codingshuttle.project.airBnb.entity.*;
import com.codingshuttle.project.airBnb.entity.enums.BookingStatus;
import com.codingshuttle.project.airBnb.exceptions.ResourceNotFoundException;
import com.codingshuttle.project.airBnb.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.aspectj.asm.IModelFilter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.print.attribute.standard.Chromaticity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final InventoryRepository inventoryRepository;
    private final ModelMapper modelMapper;
    private final GuestRepository guestRepository;

    @Override
    @Transactional
    public BookingDTO initialiseBooking(BookingRequestDTO bookingRequestDTO) {
        log.info("Initialise booking by BookingRequestDTO id:{}", bookingRequestDTO);
        Hotel hotel=hotelRepository.findById(bookingRequestDTO.getRoomId()).orElseThrow(()->
                new ResourceNotFoundException("Hotel Not Found while we initialise booking by this HotelId: "+bookingRequestDTO.getHotelId())
                );

        Room room=roomRepository.findById(bookingRequestDTO.getRoomId()).orElseThrow(()->
                        new ResourceNotFoundException("Room Not Found while we initialise booking by this RoomId: "+bookingRequestDTO.getRoomId())
                );
        List<Inventory> inventoryList=inventoryRepository.findAndLockAvailableInventory(
                room.getId(),bookingRequestDTO.getCheckInDate(),bookingRequestDTO.getCheckOutDate(),bookingRequestDTO.getRoomCount());

        long dayCount = ChronoUnit.DAYS.between(bookingRequestDTO.getCheckInDate(),bookingRequestDTO.getCheckOutDate())+1;

        if(inventoryList.size() < dayCount){
            throw new IllegalArgumentException("Room is not available anymore");
        }

        for(Inventory inventory:inventoryList){
            inventory.setReversedCount(inventory.getReversedCount() + bookingRequestDTO.getRoomCount());
        }
        inventoryRepository.saveAll(inventoryList);


        Booking booking=Booking.builder()
                .status(BookingStatus.RESERVED)
                .hotel(hotel)
                .room(room)
                .checkInDate(bookingRequestDTO.getCheckInDate())
                .checkOutDate(bookingRequestDTO.getCheckOutDate())
                .user(getCurrentUser())
                .roomCount(bookingRequestDTO.getRoomCount())
                .amount(BigDecimal.TEN)
                .build();
        booking = bookingRepository.save(booking);
        return modelMapper.map(booking,BookingDTO.class);
    }

    @Override
    @Transactional
    public BookingDTO addGuests(Long bookingId, List<GuestDTO> guestDTOList) {
        log.info("Adding guests for booking with id:{}",bookingId);

        Booking booking=bookingRepository.findById(bookingId).orElseThrow(()->
                new ResourceNotFoundException("Booking not found with id:"+bookingId)
                );
        if(hasBookingExperied(booking)){
            throw  new IllegalArgumentException("Booking is already expired");
        }
        if(booking.getStatus() != BookingStatus.RESERVED){
            throw new IllegalArgumentException("Booking is not under reserved state,cannot add guests");
        }
        for(GuestDTO guestDTO:guestDTOList){
            Guest guest=modelMapper.map(guestDTO,Guest.class);
            guest.setUser(getCurrentUser());
            guest=guestRepository.save(guest);
            booking.getGuests().add(guest);
        }
        booking.setStatus(BookingStatus.RESERVED);
        booking=bookingRepository.save(booking);
        return modelMapper.map(booking,BookingDTO.class);
    }

    public Boolean hasBookingExperied(Booking booking){
      return booking.getCreatedAt().plusMinutes(10).isBefore(LocalDateTime.now());
        }

        public User getCurrentUser(){
            User user =new User();
            user.setId(1L);
            return user;
        }
}
