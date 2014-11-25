package com.qubaopen.doctor.controller;

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttributes

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.doctor.repository.DoctorBookingRepository
import com.qubaopen.survey.entity.doctor.Doctor
import com.qubaopen.survey.entity.doctor.DoctorBooking

@RestController
@RequestMapping('doctorBooking')
@SessionAttributes('currentDoctor')
public class DoctorBookingController extends AbstractBaseController<DoctorBooking, Long> {

	private static Logger logger = LoggerFactory.getLogger(DoctorBookingController.class)
	
	@Autowired
	DoctorBookingRepository doctorBookingRepository
	
	@Override
	protected MyRepository<DoctorBooking, Long> getRepository() {
		return doctorBookingRepository;
	}

	@RequestMapping(value = 'retrieveBookingInfo', method = RequestMethod.GET)
	retrieveBookingInfo(@ModelAttribute('currentDoctor') Doctor doctor) {
		
		def bookingList = doctorBookingRepository.findAll(
			[
				doctor_equal : doctor	
			]
		)
		
		def data = []
		bookingList.each {
			data << [
				'userId' : it.user?.id,
				'userName' : it.user?.userInfo?.name,
				'helpReason' : it.refusalReason,
				'refusalReason' : it.refusalReason,
				'time' : it.time,
				'quick' : it.quick,
				'consultType' : it.consultType?.ordinal(),
				'status' : it.status?.ordinal(),
				'money' : it.money
			]
		}
		[
			'success' : '1',
			'data' : data	
		]
	}
	
	@RequestMapping(value = 'retrieveDetailInfo/{id}', method = RequestMethod.GET)
	retrieveDetailInfo(@PathVariable('id') Long id) {
		
		logger.trace('-- 查看单个信息 --')
		
		def booking = doctorBookingRepository.findOne(id)
		
		if (booking) {
			[
				'success' : '1',
				'userName' : booking.user?.userInfo?.name,
				'userSex' : booking.user?.userInfo?.sex?.ordinal(),
				'birthday' : booking.user?.userInfo?.birthday,
				'helpReason' : booking.helpReason
			]
		} else {
			'{"success" : "0"}'
		}
	}
	
}
