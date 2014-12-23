package com.qubaopen.doctor.repository.booking;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.booking.BookingSelfTime;

public interface BookingSelfTimeRepository extends MyRepository<BookingSelfTime, Long> {
	
	int deleteById(long id);

}
