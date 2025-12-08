package com.booking.controller;

import com.booking.domain.BookingStatus;
import com.booking.domain.PaymentMethod;
import com.booking.dto.*;
import com.booking.mapper.BookingMapper;
import com.booking.model.Booking;
import com.booking.model.SalonReport;
import com.booking.service.BookingService;


import com.booking.service.client.SalonFeignClient;
import com.booking.service.client.PaymentFeignClient;
import com.booking.service.client.ServiceFeignClient;
import com.booking.service.client.UserFeignClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/bookings")
public class BookingController {

    @Autowired
    BookingService bookingService;

    @Autowired
    UserFeignClient userFeignClient;

    @Autowired
    SalonFeignClient salonFeignClient;

    @Autowired
    PaymentFeignClient paymentFeignClient;

    @Autowired
    ServiceFeignClient serviceFeignClient;

    @PostMapping
    public ResponseEntity<PaymentLinkResponse> createBooking(@RequestParam Long salonId,
                                                 @RequestParam PaymentMethod paymentMethod,
                                                 @RequestBody BookingRequest bookingRequest,
                                                 @RequestHeader("Authorization") String jwt) throws Exception {

        UserDTO user = userFeignClient.getUserFromJwtToken(jwt).getBody();

        SalonDTO salon = salonFeignClient.getSalonById(salonId).getBody();


        Set<ServiceDTO> serviceDTOSet = serviceFeignClient.getServiceByIds(bookingRequest.getServiceIds()).getBody();

        if(serviceDTOSet.isEmpty()){
            throw new Exception("service not found...");
        }


        Booking booking = bookingService.createBooking(bookingRequest, salon,
                user,
                serviceDTOSet);

        BookingDTO bookingDTO = BookingMapper.toDto(booking);

        PaymentLinkResponse response = paymentFeignClient.createPaymentLink(bookingDTO, paymentMethod, jwt).getBody();

        return ResponseEntity.ok(response);

    }

    @GetMapping("/customer")
    public ResponseEntity<Set<BookingDTO>> getBookingsByCustomer(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {

        UserDTO user = userFeignClient.getUserFromJwtToken(jwt).getBody();

        if(user == null){
            throw new Exception("user not found with this "+ user);
        }

        List<Booking> bookings = bookingService.getBookingByCustomer(user.getId());
        return ResponseEntity.ok(getBookingDTOs(bookings));

    }

    @GetMapping("/salon")
    public ResponseEntity<Set<BookingDTO>> getBookingsBySalon(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {

        SalonDTO salon = salonFeignClient.getSalonByOwnerId(jwt).getBody();
        if(salon == null){
            throw new Exception("salon not found with this jwt...");
        }
        List<Booking> bookings = bookingService.getBookingBySalon(salon.getId());
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
            @PathVariable Long bookingId,
            @RequestParam BookingStatus status
            ) throws Exception {
        Booking booking = bookingService.updateBooking(status, bookingId);

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
    public ResponseEntity<SalonReport> getSalonReport(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        SalonDTO salon = salonFeignClient.getSalonByOwnerId(jwt).getBody();
        if(salon == null){
            throw new Exception("salon not found");
        }
        SalonReport report = bookingService.getSalonReport(salon.getId());
        return ResponseEntity.ok(report);
    }

}
