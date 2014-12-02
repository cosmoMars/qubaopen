package com.qubaopen.doctor.controller.doctor;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.doctor.repository.doctor.DoctorBookingTimeRepository
import com.qubaopen.survey.entity.doctor.Doctor
import com.qubaopen.survey.entity.doctor.DoctorBookingTime


@RestController
@RequestMapping('doctorBookingTime')
@SessionAttributes('currentDoctor')
public class DoctorBookingTimeController extends AbstractBaseController<DoctorBookingTime, Long> {

	@Autowired
	DoctorBookingTimeRepository doctorBookingTimeRepository
	
	@Override
	MyRepository<DoctorBookingTime, Long> getRepository() {
		return doctorBookingTimeRepository
	}

	/**
	 * @param time
	 * @param strTime
	 * @param doctor
	 * @return
	 *
	 */
	@RequestMapping(value = 'retrieveBookingTime', method = RequestMethod.GET)
	retrieveBookingTime(@RequestParam(required = false) String time, @RequestParam(required = false) String strTime, @ModelAttribute('currentDoctor') Doctor doctor) {
		
		
	}
	
	/**
	 * @return
	 */
	@RequestMapping(value = 'addOccupyTime', method = RequestMethod.POST)
	addOccupyTime(@RequestParam(required = false) String content,
		@RequestParam(required = false) String startTime,
		@RequestParam(required = false) String endTime,
		@ModelAttribute('currentDoctor') Doctor doctor) {
		
		def doctorBookingTime = new DoctorBookingTime(
			doctor : doctor,
			startTime : DateUtils.parseDate(startTime, 'yyyy-MM-dd HH'),
			endTime : DateUtils.parseDate(endTime, 'yyyy-MM-dd HH'),
			content : content
		)
		
		doctorBookingTimeRepository.save(doctorBookingTime)
		'{"success" : "1"}'
	}


}
