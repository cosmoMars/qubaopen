package com.qubaopen.doctor.controller.hospital;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.doctor.repository.doctor.BookingRepository;
import com.qubaopen.survey.entity.booking.Booking;
import com.qubaopen.survey.entity.hospital.Hospital;

@RestController
@RequestMapping("hospitalBooking")
@SessionAttributes("currentHospital")
public class HospitalBookingController extends AbstractBaseController<Booking, Long> {

	private static Logger logger = LoggerFactory.getLogger(HospitalBookingController.class);

	@Autowired
	private BookingRepository bookingRepository;

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
		result.put("userSex", booking.getSex() != null ? booking.getSex() : "");
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

}
