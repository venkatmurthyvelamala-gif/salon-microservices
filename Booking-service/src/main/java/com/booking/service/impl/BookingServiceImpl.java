package com.booking.service.impl;

import com.booking.domain.BookingStatus;
import com.booking.dto.BookingRequest;
import com.booking.dto.SalonDTO;
import com.booking.dto.ServiceDTO;
import com.booking.dto.UserDTO;
import com.booking.model.Booking;
import com.booking.model.SalonReport;
import com.booking.repository.BookingRepository;
import com.booking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    BookingRepository bookingRepository;

    @Override
    public Booking createBooking(BookingRequest booking,
                                 SalonDTO salon,
                                 UserDTO user,
                                 Set<ServiceDTO> serviceDTOSet) throws Exception {

        int totalDuration = serviceDTOSet.stream()
                .mapToInt(ServiceDTO::getDuration).sum();

        LocalDateTime bookingStartTime = booking.getStartTime();
        LocalDateTime bookingCloseTime = bookingStartTime.plusMinutes(totalDuration);

        Boolean isSlotAvailable = isTimeSlotAvailable(salon,bookingStartTime,bookingCloseTime);

        Double totalPrice = (double) serviceDTOSet.stream()
                        .mapToInt(ServiceDTO::getPrice).sum();

        Set<Long> idsList = serviceDTOSet.stream()
                .map(ServiceDTO::getId).collect(Collectors.toSet());

        Booking newBooking = new Booking();

        newBooking.setCustomerId(user.getId());
        newBooking.setSalonId(salon.getId());
        newBooking.setServiceIds(idsList);
        newBooking.setStatus(BookingStatus.PENDING);
        newBooking.setStartTime(bookingStartTime);
        newBooking.setEndTime(bookingCloseTime);
        newBooking.setTotalPrice(totalPrice);

        return bookingRepository.save(newBooking);
    }

    public Boolean isTimeSlotAvailable(SalonDTO salonDto,
                                       LocalDateTime bookingStartTime,
                                       LocalDateTime bookingEndTime) throws Exception {


        List<Booking> existingBookings = getBookingBySalon(salonDto.getId());
        LocalDateTime salonOpenTime = salonDto.getOpenTime().atDate(bookingStartTime.toLocalDate());
        LocalDateTime salonCloseTime =salonDto.getClosTime().atDate(bookingEndTime.toLocalDate());

        if(bookingStartTime.isBefore(salonOpenTime) || bookingEndTime.isAfter(salonCloseTime)){
            throw new Exception("Booking must be within salon working hours");
        }

        for(Booking existBooking:existingBookings){
            LocalDateTime existingBookingStartTime = existBooking.getStartTime();
            LocalDateTime existingBookingEndTime = existBooking.getEndTime();

            if(bookingStartTime.isBefore(existingBookingEndTime) &&
               bookingEndTime.isAfter(existingBookingStartTime)){
                throw new Exception("slot is not avaialbe choose different Timings");
            }
            if(bookingStartTime.isEqual(existingBookingStartTime) ||
                    bookingEndTime.isEqual(existingBookingEndTime)){
                throw new Exception("slot is not avaialbe choose different Timings");
            }
        }
        return true;

    }



    @Override
    public List<Booking> getBookingByCustomer(Long customerId) {
        return bookingRepository.findByCustomerId(customerId);
    }

    @Override
    public List<Booking> getBookingBySalon(Long salonId) {
        return bookingRepository.findBySalonId(salonId);
    }

    @Override
    public Booking getBookingById(Long id) throws Exception {
        Booking booking = bookingRepository.findById(id).orElse(null);
        if(booking == null){
            throw new Exception("Booking not found with this id"+ id);
        }
        return  booking;
    }

    @Override
    public Booking updateBooking(BookingStatus status, Long bookingId) throws Exception {
        Booking updateBook = getBookingById(bookingId);
        updateBook.setStatus(status);
        return bookingRepository.save(updateBook);
    }

    @Override
    public List<Booking> getBookingByDate(LocalDate date, Long salonId) {
        List<Booking> allBookings = getBookingBySalon(salonId);

        if(date == null){
            return allBookings;
        }

        return allBookings.stream().filter(booking -> isSameDate(booking.getStartTime(), date) ||
                isSameDate(booking.getEndTime(), date)).collect(Collectors.toList());
    }



    private boolean isSameDate(LocalDateTime dateTime, LocalDate date) {

        return dateTime.toLocalDate().isEqual(date);
    }

    @Override
    public SalonReport getSalonReport(Long salonId) {

        List<Booking> bookings = getBookingBySalon(salonId);

        int totalEarning = (int) bookings.stream()
                .mapToDouble(Booking::getTotalPrice).sum();

        int totalBooking = bookings.size();

        List<Booking> cancelledBooking = bookings.stream()
                .filter(booking -> booking.getStatus().equals(BookingStatus.CANCELLED))
                .collect(Collectors.toList());

        Double totalRefund = cancelledBooking.stream()
                .mapToDouble(Booking::getTotalPrice).sum();

        SalonReport salonReport = new SalonReport();
        salonReport.setSalonId(salonId);
        salonReport.setSalonName(salonReport.getSalonName());
        salonReport.setCancelledBookings(cancelledBooking.size());
        salonReport.setTotalBookings(totalBooking);
        salonReport.setTotalEarnings(totalEarning);
        salonReport.setTotalRefund(totalRefund);

        return salonReport;
    }
}
