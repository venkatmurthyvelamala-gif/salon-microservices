package com.booking.controller;

import com.booking.domain.BookingStatus;
import com.booking.dto.*;
import com.booking.mapper.BookingMapper;
import com.booking.model.Booking;
import com.booking.model.SalonReport;
import com.booking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/booking")
public class BookingController {

    @Autowired
    BookingService bookingService;

    public ResponseEntity<Booking> createBooking(@RequestParam Long salonId,
                                                 @RequestBody BookingRequest bookingRequest) throws Exception {

        UserDTO user = new UserDTO();
        user.setId(1L);

        SalonDTO salon = new SalonDTO();
        salon.setId(salonId);
        salon.setOpenTime(LocalTime.now());
        salon.setClosTime(LocalTime.now().plusHours(12));

        Set<ServiceDTO> serviceDTOSet = new HashSet<>();

        ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.setId(1L);
        serviceDTO.setName("Hair cut for men");
        serviceDTO.setPrice(400);
        serviceDTO.setDuration(45);

        serviceDTOSet.add(serviceDTO);

        Booking booking = bookingService.createBooking(bookingRequest, salon,
                user,
                serviceDTOSet);

        return ResponseEntity.ok(booking);

    }

    @GetMapping("/customer")
    public ResponseEntity<Set<BookingDTO>> getBookingsByCustomer(){

        List<Booking> bookings = bookingService.getBookingByCustomer(1L);
        return ResponseEntity.ok(getBookingDTOs(bookings));

    }

    @GetMapping("/salon")
    public ResponseEntity<Set<BookingDTO>> getBookingsBySalon(){

        List<Booking> bookings = bookingService.getBookingBySalon(1L);
        return ResponseEntity.ok(getBookingDTOs(bookings));

    }

    private Set<BookingDTO>  getBookingDTOs(List<Booking> bookings){

        return bookings.stream().map(booking -> {
            return BookingMapper.toDto(booking);
        }).collect(Collectors.toSet());

    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable Long bookingId) throws Exception {

        Booking booking = bookingService.getBookingById(bookingId);
        return ResponseEntity.ok(BookingMapper.toDto(booking));

    }

    @PutMapping("/{bookingId}/status")
    public  ResponseEntity<BookingDTO> updateBookingStatus(
            @PathVariable Long bookinId,
            @RequestParam BookingStatus status
            ) throws Exception {
        Booking booking = bookingService.updateBooking(status, bookinId);

        return ResponseEntity.ok(BookingMapper.toDto(booking));

    }

    @GetMapping("/slot/salon/{salonId}/date/{date}")
    public ResponseEntity<List<BookingSlotDTO>> getBookedSlot(
            @PathVariable Long salonId,
            @RequestParam LocalDate date
    ){

        List<Booking> bookings = bookingService.getBookingByDate(date, salonId);

        List<BookingSlotDTO> slotDTOs = bookings.stream()
                .map(booking ->{
                    BookingSlotDTO slotDTO = new BookingSlotDTO();
                    slotDTO.setStartTime(booking.getStartTime());
                    slotDTO.setEndTime(booking.getEndTime());
                    return  slotDTO;
                }).collect(Collectors.toList());

        return ResponseEntity.ok(slotDTOs);
    }

    @GetMapping("/report")
    public ResponseEntity<SalonReport> getSalonReport(){
        SalonReport report = bookingService.getSalonReport(1L);
        return ResponseEntity.ok(report);
    }








}
