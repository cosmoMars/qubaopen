package com.qubaopen.doctor.controller.hospital;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.doctor.repository.doctor.BookingRepository;
import com.qubaopen.doctor.repository.hospital.HospitalInfoRepository;
import com.qubaopen.survey.entity.booking.Booking;
import com.qubaopen.survey.entity.booking.ResolveType;
import com.qubaopen.survey.entity.doctor.Doctor;
import com.qubaopen.survey.entity.doctor.DoctorInfo;
import com.qubaopen.survey.entity.hospital.Hospital;
import com.qubaopen.survey.entity.hospital.HospitalInfo;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.*;

@RestController
@RequestMapping("hospitalBooking")
@SessionAttributes("currentHospital")
public class HospitalBookingController extends AbstractBaseController<Booking, Long> {

	private static Logger logger = LoggerFactory.getLogger(HospitalBookingController.class);

	@Autowired
	private BookingRepository bookingRepository;
	
	@Autowired
	private HospitalInfoRepository hospitalInfoRepository;

	@Override
	protected MyRepository<Booking, Long> getRepository() {
		return bookingRepository;
	}

	/**
	 * @param idx
	 * @param pageable
	 * @param hospital
	 * @return 获取诊所订单
	 */
	@RequestMapping(value = "retrieveHospitalBooking", method = RequestMethod.POST)
	private Map<String, Object> retrieveHospitalBooking(@RequestParam(required = false) Integer idx,
			@PageableDefault(page = 0, size = 20, sort = "createdDate", direction = Direction.DESC) Pageable pageable,
			@ModelAttribute("currentHospital") Hospital hospital) {

		logger.trace("-- 获取诊所订单 --");

		Map<String, Object> filters = new HashMap<String, Object>();
		if (idx != null) {
			filters.put("status_equal", Booking.Status.values()[idx]);
		}
		filters.put("hospital_equal", hospital);

		Page<Booking> bookings = bookingRepository.findAll(filters, pageable);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", "1");

		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

		for (Booking booking : bookings) {
			Map<String, Object> bmap = new HashMap<String, Object>();
			bmap.put("bookingId", booking.getId() != null ? booking.getId() : "");
			bmap.put("userId", booking.getUser() != null ? booking.getUser().getId() : "");
			bmap.put("userName", booking.getName() != null ? booking.getName() : "");
			bmap.put("helpReason", booking.getRefusalReason() != null ? booking.getRefusalReason() : "");
			bmap.put("time", booking.getTime() != null ? booking.getTime() : "");
			bmap.put("quick", booking.isQuick());
			bmap.put("consultType", booking.getConsultType() != null ? booking.getConsultType().ordinal() : "");
			bmap.put("status", booking.getStatus() != null ? booking.getStatus().ordinal() : "");
			bmap.put("money", booking.getMoney());
			bmap.put("userStatus", booking.getUserStatus() != null ? booking.getUserStatus().ordinal() : "");
			bmap.put("doctorStatus", booking.getDoctorStatus() != null ? booking.getDoctorStatus().ordinal() : "");
			data.add(bmap);
		}
		result.put("data", data);
		result.put("more", bookings.hasNext());

		return result;
	}

	/**
	 * @param id
	 * @param hospital
	 * @return 获取订单信息
	 */
	@RequestMapping(value = "retrieveDetailInfo/{id}", method = RequestMethod.GET)
	private Map<String, Object> retrieveDetailInfo(@PathVariable("id") long id, @ModelAttribute("currentHospital") Hospital hospital) {

		logger.trace("-- 获取订单信息 --");

		Booking booking = bookingRepository.findOne(id);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", "1");
		result.put("userId", booking.getUser() != null ? booking.getUser().getId() : "");
		result.put("userName", booking.getName() != null ? booking.getName() : "");
		result.put("phone", booking.getPhone() != null ? booking.getPhone() : "");
		result.put("userSex", booking.getSex() != null ? booking.getSex().ordinal() : "");
		result.put("birthday", booking.getBirthday() != null ? booking.getBirthday() : "");
		result.put("profession", booking.getProfession() != null ? booking.getProfession() : "");
		result.put("city", booking.getCity() != null ? booking.getCity() : "");
		result.put("married", booking.isMarried());
		result.put("haveChildren", booking.isHaveChildren());
		result.put("helpReason", booking.getHelpReason() != null ? booking.getHelpReason() : "");
		result.put("otherProblem", booking.getOtherProblem() != null ? booking.getOtherProblem() : "");
		result.put("treatmented", booking.isTreatmented());
		result.put("haveConsulted", booking.isHaveConsulted());
		result.put("userStatus", booking.getStatus() != null ? booking.getStatus().ordinal() : "");
		result.put("doctorStatus", booking.getDoctorStatus() != null ? booking.getDoctorStatus().ordinal() : "");
		return result;
	}

	/**
	 * 日历列表
	 * @param time
	 * @param hospital
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "retrieveWeeklySchedule", method = RequestMethod.GET)
	private Map<String, Object> retrieveWeeklySchedule(@RequestParam(required = false) String time,
			@ModelAttribute("currentHospital") Hospital hospital) throws ParseException {

		logger.trace("-- 订单日历 --");
		
		Date date = null;
		
		if (time == null) {
			date = new Date();
		} else {
			date = DateUtils.parseDate(time, "yyyy-MM-dd");
		}
		HospitalInfo hospitalInfo = hospitalInfoRepository.findOne(hospital.getId());
		
		String bookingTime = null;

		if (hospitalInfo != null) // 拆分
			bookingTime = hospitalInfo.getBookingTime();
		String[] times = bookingTime.split(",");
		
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		// 循环7天
		for (int i = 0; i < 7; i++) {
			
			List<Map<String, Object>> timeData = new ArrayList<Map<String,Object>>();
			if (i > 0)
				c.add(Calendar.DATE, 1);
			Date day = c.getTime();
			int	idx = dayForWeek(day);
			String timeModel = times[idx - 1];
			
			// 默认占用的时间
			for (int j = 0; j < timeModel.length(); j++) {
				if ('1' == timeModel.charAt(j) || "1".equals(timeModel.charAt(j))) {
					Map<String, Object> map = new HashMap<String, Object>();
//					map.put("type", 3);
					map.put("startTime", DateFormatUtils.format(DateUtils.parseDate(j +":00", "HH:mm"), "HH:mm"));
					map.put("endTime", DateFormatUtils.format(DateUtils.parseDate((j + 1) +":00", "HH:mm"), "HH:mm"));
					timeData.add(map);
				}
			}
			
//			// 循环订单
//			for (int j = 0; j < bookingList.size(); j++) {
//				Calendar cal = Calendar.getInstance();
//				cal.setTime(bookingList.get(j).getTime());
//				cal.add(Calendar.HOUR, 1);
//				Date endTime = cal.getTime();
//				
//				Map<String, Object> map = new HashMap<String, Object>();
//				Booking booking = bookingList.get(j);
//				
//				map.put("type", 2);
//				map.put("bookingId", booking.getId());
//				map.put("name", booking.getName() != null ? booking.getName() : "");
//				map.put("helpReason", booking.getHelpReason() != null ? booking.getHelpReason() : "");
//				map.put("consultType", booking.getConsultType() != null ? booking.getConsultType().ordinal() : "");
//				map.put("startTime", booking.getTime() != null ? DateFormatUtils.format(booking.getTime(), "HH:mm") : "");
//				map.put("endTime", endTime != null ? DateFormatUtils.format(endTime, "HH:mm") : "");
//				map.put("quick", booking.isQuick());
//				map.put("status", booking.getStatus() != null ? booking.getStatus().ordinal() : "");
//				timeData.add(map);
//				
//			}
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("dayOfWeek", idx);
			data.put("day", DateFormatUtils.format(c.getTime(), "yyyy-MM-dd"));
			data.put("timeData", timeData);
			resultList.add(data);
		}
		result.put("success", "1");
		result.put("result", resultList);
		return result;
	}

	/**
	 * @param id
	 * @param index
	 * @param doctor
	 * @return
	 * 修改订单状态
	 */
	@Transactional
	@RequestMapping(value = "modifyBookingStatus", method = RequestMethod.POST)
	private String modifyBookingStatus(@RequestParam(required = false) Long id,
						@RequestParam(required = false) String content,
						@RequestParam(required = false) String bookingTime,
						@RequestParam(required = false) Integer index,
						@ModelAttribute("currentHospital") Hospital hospital) {

		logger.trace("-- 修改订单状态 --");

		HospitalInfo hi = hospitalInfoRepository.findOne(hospital.getId());

		if (hi.getLoginStatus() != HospitalInfo.LoginStatus.Audited) {
			return "{\"success\" : \"0\", \"message\" : \"err923\"}";
		}

		Booking booking = bookingRepository.findOne(id);
		Booking.Status bookingStatus = Booking.Status.values()[index];


//		if (bookingStatus == Booking.Status.Accept) {
//			String param1 = "{\"url\" : \"http://zhixin.me/smsRedirect.html\"}";
//			smsService.sendSmsMessage(booking.getPhone(), 5, param1);
//		}
		booking.setStatus(bookingStatus);

		if (bookingStatus !=  null && bookingStatus == Booking.Status.Refusal) {
			booking.setRefusalReason(content);

//			payEntityRepository.deleteByBooking(booking)

		}else if (bookingStatus !=  null && bookingStatus == Booking.Status.Paying) {

			try {
				booking.setTime(DateUtils.parseDate(bookingTime, "yyyy-MM-dd HH:mm"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
		

		if (bookingStatus != null && bookingStatus == Booking.Status.Next) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(booking.getTime());

			cal.add(Calendar.DATE, 7);
			Date newTime = cal.getTime();
			booking.setTime(newTime);
		}
		booking.setLastModifiedDate(new DateTime());
		booking.setResolveType(ResolveType.None);
		booking.setSendEmail(false);
		bookingRepository.save(booking);
		return "{\"success\" : \"1\"}";
	}



	int dayForWeek(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int idx = 0;
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			idx = 7;
		} else {
			idx = c.get(Calendar.DAY_OF_WEEK) - 1;
		}
		return idx;
	}
	
}
