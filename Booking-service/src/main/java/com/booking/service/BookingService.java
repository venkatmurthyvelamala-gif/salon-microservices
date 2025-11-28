package com.booking.service;

import com.booking.domain.BookingStatus;
import com.booking.dto.BookingRequest;
import com.booking.dto.SalonDTO;
import com.booking.dto.ServiceDTO;
import com.booking.dto.UserDTO;
import com.booking.model.Booking;
import com.booking.model.SalonReport;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface BookingService {

     Booking createBooking(BookingRequest booking,
                           SalonDTO salon,
                           UserDTO user,
                           Set<ServiceDTO> serviceDTOSet) throws Exception;

     List<Booking> getBookingByCustomer(Long customerId);
     List<Booking> getBookingBySalon(Long salonId);
     Booking getBookingById(Long id) throws Exception;
     Booking updateBooking(BookingStatus status, Long bookingId) throws Exception;
     List<Booking> getBookingByDate(LocalDate date, Long salonId);
     SalonReport getSalonReport(Long salonId);



}
