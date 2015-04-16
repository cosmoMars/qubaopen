package com.qubaopen.backend.repository.booking;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.booking.Booking;
import com.qubaopen.survey.entity.booking.BookingProcessLog;

/**
 * Created by mars on 15/3/26.
 */
public interface BookingProcessLogRepository extends MyRepository<BookingProcessLog, Long> {
	@Query("from BookingProcessLog bb where bb.booking = :booking order by bb.createdDate desc")
	List<BookingProcessLog> findListByBooking(Pageable pageable,@Param("booking") Booking booking);
}
