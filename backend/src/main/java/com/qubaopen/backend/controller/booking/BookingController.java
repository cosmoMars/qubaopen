package com.qubaopen.backend.controller.booking;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.backend.repository.booking.BookingProcessLogRepository;
import com.qubaopen.backend.repository.booking.BookingRepository;
import com.qubaopen.backend.repository.doctor.AssistantRepository;
import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.booking.Booking;
import com.qubaopen.survey.entity.booking.BookingProcessLog;
import com.qubaopen.survey.entity.booking.ResolveType;
import com.qubaopen.survey.entity.doctor.Assistant;

@RestController
@RequestMapping("booking")
public class BookingController extends AbstractBaseController<Booking, Long> {

	@Autowired
	private BookingRepository bookingRepository;
	
	@Autowired
	private BookingProcessLogRepository bookingProcessLogRepository;
	
	@Autowired
	private AssistantRepository assistantRepository;
	
	
	@Override
	protected MyRepository<Booking, Long> getRepository() {
		return bookingRepository;
	}

	
	/**
	 * 获取订单 根据resolveType or id
	 * @param pageable
	 * @param type
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "retrieveBookings", method = RequestMethod.GET)
	private Object retrieveBookings(@PageableDefault(page = 0, size = 20)
			Pageable pageable,
			@RequestParam(required = false) Integer type,
			@RequestParam(required = false) Long id) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		
		List<Booking> bookings;
		
		if(id!=null){
			/*如果传了参数id  则查询一条*/
			bookings = bookingRepository.findBookingById(id);
		}else{
			/*如果没有id 按照type查询*/
			bookings = bookingRepository.findBookingByStatus(pageable, ResolveType.values()[type.intValue()]);
		}
		
		
		result.put("success", "1");
		result.put("list", bookings);
		return result;

	}

	/**
	 * 处理订单
	 * @param bookingId
	 * @param assistantId
	 * @param resolved
	 * @param remark
	 * @return
	 */
	@RequestMapping(value = "resolveBooking", method = RequestMethod.POST)
	private Object resolveBooking(@RequestParam long bookingId,
			@RequestParam long assistantId,
			@RequestParam(required = false) boolean resolved,
			@RequestParam(required = false) String remark
			) {
		
		Map<String, Object> result = new HashMap<String, Object>();	
		Booking booking = bookingRepository.findOne(bookingId);
		
		if(null != booking){
			BookingProcessLog bookingProcessLog=new BookingProcessLog();
			Assistant assistant=assistantRepository.findOne(assistantId);
			bookingProcessLog.setBooking(booking);
			bookingProcessLog.setRemark(remark);
			bookingProcessLog.setResolveType(booking.getResolveType());
			bookingProcessLog.setAssistant(assistant);
			
			if(resolved){
				booking.setResolveType(ResolveType.None);
				booking.setSendEmail(false);
				bookingRepository.save(booking);
			}	
			
			bookingProcessLogRepository.save(bookingProcessLog);
		}
		
		result.put("success", "1");
		return result;

	}
}
