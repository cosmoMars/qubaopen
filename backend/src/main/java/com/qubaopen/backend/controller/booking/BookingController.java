package com.qubaopen.backend.controller.booking;

import com.qubaopen.backend.repository.assistant.AssistantRepository;
import com.qubaopen.backend.repository.booking.BookingProcessLogRepository;
import com.qubaopen.backend.repository.booking.BookingRepository;
import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.booking.Booking;
import com.qubaopen.survey.entity.booking.BookingProcessLog;
import com.qubaopen.survey.entity.booking.ResolveType;
import com.qubaopen.survey.entity.doctor.Assistant;
import com.qubaopen.survey.entity.doctor.Doctor;
import com.qubaopen.survey.entity.hospital.Hospital;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		

		Doctor doctor;
		Hospital hospital;
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		for (Booking booking : bookings) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", booking.getId());
			map.put("createDate", booking.getCreatedDate().toDate());
			map.put("lastModifiedDate", booking.getLastModifiedDate().toDate());
			map.put("birthday", booking.getBirthday());
			map.put("city", booking.getCity());
			map.put("consultType", booking.getConsultType()==null?"":booking.getConsultType().ordinal());
			map.put("haveChildren", booking.isHaveChildren());
			map.put("haveConsulted", booking.isHaveConsulted());
			map.put("helpReason", booking.getHelpReason());
			map.put("married", booking.isMarried());
			map.put("money", booking.getMoney());
			map.put("quickMoney", booking.getQuickMoney());
			map.put("name", booking.getName());
			map.put("otherProblem", booking.getOtherProblem());
			map.put("phone", booking.getPhone());
			map.put("profession", booking.getProfession());
			map.put("quick", booking.isQuick());
			map.put("refusalReason", booking.getRefusalReason());
			map.put("sex", booking.getSex());
			map.put("status", booking.getStatus()==null?"":booking.getStatus().ordinal());
			map.put("time", booking.getTime());
			map.put("treatmented", booking.isTreatmented());
			map.put("userId", booking.getUser().getId());
			map.put("payTime", booking.getPayTime());
			map.put("tradeNo", booking.getTradeNo());
			map.put("doctorStatus", booking.getDoctorStatus());
			map.put("userStatus", booking.getUserStatus());
			map.put("lastBookingTime", booking.getLastBookingTime());
			map.put("chargeId", booking.getChargeId());
			map.put("outDated", booking.getOutDated());
			map.put("sendDoctor", booking.isSendDoctor());
			map.put("sendUser", booking.isSendUser());
			map.put("resolveType", booking.getResolveType());
			map.put("sendEmail", booking.isSendEmail());
			
			doctor=booking.getDoctor();
			if(doctor!=null){
				map.put("doctorId", doctor.getId());
				map.put("doctorName", doctor.getDoctorInfo().getName());
				map.put("doctorPhone", doctor.getPhone());
				map.put("doctorInfoPhone", doctor.getDoctorInfo().getPhone());
			}else{
				map.put("doctorId", "");
				map.put("doctorName", "");
				map.put("doctorPhone", "");
				map.put("doctorInfoPhone", "");
		
			}
			
			hospital=booking.getHospital();
			if(hospital!=null){
				map.put("hospitalId", hospital.getId());
				map.put("hospitalName", hospital.getHospitalInfo().getName());
				map.put("hospitalPhone", hospital.getHospitalInfo().getPhone());
			}else{
				map.put("hospitalId", "");
				map.put("hospitalName", "");
				map.put("hospitalPhone", "");
			}
			
			list.add(map);
		}
		
		result.put("success", "1");
		result.put("list", list);
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
	@Transactional
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
	

	/**
	 * 获取订单处理记录
	 * @param pageable
	 * @param type
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "retrieveBookingProcessLogs", method = RequestMethod.GET)
	private Object retrieveBookingProcessLogs(@PageableDefault(page = 0, size = 20)
			Pageable pageable,
			@RequestParam(required = false) Long bookingId) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		
		List<BookingProcessLog> bookingProcessLogs;
		
		Booking booking=bookingRepository.findOne(bookingId);		
		if(booking==null){
			result.put("success", "0");
			result.put("message", "没有该订单");
		}
		
		bookingProcessLogs = bookingProcessLogRepository.findListByBooking(pageable,booking);
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		for (BookingProcessLog bookingProcessLog : bookingProcessLogs) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id",bookingProcessLog.getId());
			map.put("assistant",bookingProcessLog.getAssistant().getName());
			map.put("remark",bookingProcessLog.getRemark());
			map.put("resolveType",bookingProcessLog.getResolveType().ordinal());
			list.add(map);
			
		}

		result.put("success", "1");
		result.put("list", list);
		return result;
		
	}
}
