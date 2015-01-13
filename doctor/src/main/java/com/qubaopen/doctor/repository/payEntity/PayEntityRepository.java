package com.qubaopen.doctor.repository.payEntity;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.booking.Booking;
import com.qubaopen.survey.entity.payment.PayEntity;

public interface PayEntityRepository extends MyRepository<PayEntity, Long> {

	int deleteByBooking(Booking booking);
}
