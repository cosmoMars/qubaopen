package com.knowheart3.service.booking;

import com.knowheart3.repository.booking.BookingRepository;
import com.qubaopen.survey.entity.booking.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by mars on 15/5/7.
 */
@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Transactional
    public void saveBooking(List<Booking> bookings) {

        bookingRepository.save(bookings);
    }

}
