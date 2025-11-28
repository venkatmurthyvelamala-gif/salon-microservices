package com.booking.mapper;

import com.booking.dto.BookingDTO;
import com.booking.model.Booking;

public class BookingMapper {

    public static BookingDTO toDto(Booking booking){

        BookingDTO bookingDTO = new BookingDTO();

        bookingDTO.setId(booking.getId());
        bookingDTO.setCustomerId(booking.getCustomerId());
        bookingDTO.setSalonId(booking.getSalonId());
        bookingDTO.setStatus(booking.getStatus());
        bookingDTO.setStartTime(booking.getStartTime());
        bookingDTO.setEndTime(booking.getEndTime());
        bookingDTO.setServiceIds(booking.getServiceIds());

        return bookingDTO;
    }
}
