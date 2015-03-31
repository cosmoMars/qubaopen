package com.qubaopen.backend.repository.booking;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.booking.Booking;
import com.qubaopen.survey.entity.booking.ResolveType;

/**
 * Created by mars on 15/3/26.
 */
public interface BookingRepository extends MyRepository<Booking, Long> {

	@Query("from Booking bb where bb.resolveType = :resolveType order by bb.createdDate desc")
	List<Booking> findBookingByStatus(Pageable pageable,@Param("resolveType") ResolveType resolveType);
	
	@Query("from Booking bb where bb.id = :id")
	List<Booking> findBookingById(@Param("id") Long id);
}
