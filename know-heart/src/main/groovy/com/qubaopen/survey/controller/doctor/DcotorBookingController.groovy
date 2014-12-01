package com.qubaopen.survey.controller.doctor;

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
import com.qubaopen.survey.entity.booking.Booking;
import com.qubaopen.survey.entity.doctor.Doctor
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.repository.doctor.DoctorBookingRepository

@RestController
@RequestMapping('doctorBooking')
@SessionAttributes('currentUser')
public class DcotorBookingController extends AbstractBaseController<Booking, Long> {

	@Autowired
	DoctorBookingRepository doctorBookingRepository
	
	@Override
	protected MyRepository<Booking, Long> getRepository() {
		return doctorBookingRepository;
	}

	
	@RequestMapping(value = 'createBooking', method = RequestMethod.POST)
	createBooking(@RequestParam(required = false) Long doctorId,
		@RequestParam(required = false) String name,
		@RequestParam(required = false) Integer sexIndex,
		@RequestParam(required = false) String birthdayStr,
		@RequestParam(required = false) String profession,
		@RequestParam(required = false) String city,
		@RequestParam(required = false) boolean married,
		@RequestParam(required = false) boolean haveChildren,
		@RequestParam(required = false) String helpReason,
		@RequestParam(required = false) String otherProblem,
		@RequestParam(required = false) boolean treatmented,
		@RequestParam(required = false) boolean haveConsulted,
		@RequestParam(required = false) String refusalReason,
		@RequestParam(required = false) boolean quick,
		@RequestParam(required = false) Integer consultTypeIndex,
		@RequestParam(required = false) Integer money,
		@ModelAttribute('currentUser') User user
		) {
		
		def sex, consultType, birthday
		def doctorBooking = new Booking(
			doctor : new Doctor(id : doctorId),
			user : user,
			name : name,
			profession : profession,
			city : city,
			married : married,
			haveChildren : haveChildren,
			helpReason : helpReason,
			otherProblem : otherProblem,
			treatmented : treatmented,
			haveConsulted : haveConsulted,
			refusalReason : refusalReason,
			quick : quick,
			time : new Date()
		)
		if (birthdayStr) {
			doctorBooking.birthday = DateUtils.parseDate(birthdayStr, 'yyyy-MM-dd')
		}
		if (money != null) {
			doctorBooking.money = money
		}
		if (sexIndex != null) {
			sex = DoctorBooking.Sex.values()[sexIndex]
			doctorBooking.sex = sex
		}
		if (consultTypeIndex != null) {
			consultType = DoctorBooking.ConsultType.values()[consultTypeIndex]
			doctorBooking.consultType = consultType
		}
		doctorBookingRepository.save(doctorBooking)
		'{"success" : "1"}'
	}
}
