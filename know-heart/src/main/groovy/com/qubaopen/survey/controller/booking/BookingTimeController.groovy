package com.qubaopen.survey.controller.booking;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttributes

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.booking.BookingTime
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.repository.booking.BookingRepository;
import com.qubaopen.survey.repository.booking.BookingTimeRepository

@RestController
@RequestMapping('bookingTime')
@SessionAttributes('currentUser')
public class BookingTimeController extends AbstractBaseController<BookingTime, Long>{

	@Autowired
	BookingTimeRepository bookingTimeRepository
	
	@Autowired
	BookingRepository bookingRepository
	
	@Override
	MyRepository<BookingTime, Long> getRepository() {
		return bookingTimeRepository
	}

	/**
	 * @param bookingId
	 * @param timeId
	 * @param user
	 * @return
	 * 添加占用时间绑定
	 */
	@RequestMapping(value = 'bindingBookingTime', method = RequestMethod.POST)
	bindingBookingTime(@RequestParam Long bookingId, @RequestParam String startTime, @ModelAttribute('currentUser') User user) {
		
		def booking = bookingRepository.findOne(bookingId),
			start = DateUtils.parseDate(startTime, 'yyyy-MM-dd HH')
		def c = Calendar.getInstance()
		c.setTime start
		c.add(Calendar.HOUR, 1)
		def end = c.getTime()
		
		def bookingTime = new BookingTime(
			booking : booking,
			user : user,
			doctor : booking?.doctor,
			startTime : start,
			endTime : end
		)
		bookingTimeRepository.save(bookingTime)
		'{"success" : "1"}'
	}
	
}
