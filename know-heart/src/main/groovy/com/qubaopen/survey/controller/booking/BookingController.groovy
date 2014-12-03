package com.qubaopen.survey.controller.booking;

import java.net.Authenticator.RequestorType;

import org.apache.commons.lang3.time.DateFormatUtils
import org.apache.commons.lang3.time.DateUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttributes

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.booking.Booking
import com.qubaopen.survey.entity.doctor.Doctor
import com.qubaopen.survey.entity.hospital.Hospital
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.repository.booking.BookingRepository
import com.qubaopen.survey.repository.booking.BookingTimeRepository
import com.qubaopen.survey.repository.doctor.DoctorInfoRepository

@RestController
@RequestMapping('booking')
@SessionAttributes('currentUser')
public class BookingController extends AbstractBaseController<Booking, Long> {

	@Autowired
	BookingRepository bookingRepository
	
	@Autowired
	DoctorInfoRepository doctorInfoRepository
	
	@Autowired
	BookingTimeRepository bookingTimeRepository
	
	@Override
	MyRepository<Booking, Long> getRepository() {
		return bookingRepository
	}
	
	/**
	 * @param doctorId
	 * @param name
	 * @param sexIndex
	 * @param birthdayStr
	 * @param profession
	 * @param city
	 * @param married
	 * @param haveChildren
	 * @param helpReason
	 * @param otherProblem
	 * @param treatmented
	 * @param haveConsulted
	 * @param refusalReason
	 * @param quick
	 * @param consultTypeIndex
	 * @param money
	 * @param user
	 * @return
	 */
	@RequestMapping(value = 'createBooking', method = RequestMethod.POST)
	createBooking(@RequestParam(required = false) Long doctorId,
		@RequestParam(required = false) Long hospitalId,
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
//		@RequestParam(required = false) String refusalReason,
		@RequestParam(required = false) boolean quick,
		@RequestParam(required = false) Integer consultTypeIndex,
		@RequestParam(required = false) Integer money,
		@ModelAttribute('currentUser') User user
		) {
		
		def sex, consultType, birthday
		def booking = new Booking(
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
//			refusalReason : refusalReason,
			quick : quick,
			time : new Date()
		)
		if (doctorId != null) {
			booking.doctor = new Doctor(id : doctorId)
		}
		if (hospitalId != null) {
			booking.hospital = new Hospital(id : hospitalId)
		}
		if (birthdayStr) {
			booking.birthday = DateUtils.parseDate(birthdayStr, 'yyyy-MM-dd')
		}
		if (money != null) {
			booking.money = money
		}
		if (sexIndex != null) {
			sex = Booking.Sex.values()[sexIndex]
			booking.sex = sex
		}
		if (consultTypeIndex != null) {
			consultType = Booking.ConsultType.values()[consultTypeIndex]
			booking.consultType = consultType
		}
		booking.status = Booking.Status.values()[0]
		booking = bookingRepository.save(booking)
		[
			'success' : '1',
			'bookingId' : booking?.id
		]
	}
		
	/**
	 * @param month
	 * @param doctorId
	 * @param user
	 * @return
	 * 用户获取医师每月列表
	 */
	@RequestMapping(value = 'retrieveMonthBooking', method = RequestMethod.GET)
	retrieveMonthBooking(@RequestParam Integer month, @RequestParam long doctorId, @ModelAttribute('currentUser') User user) {

		if (month == null) {
			month = new Date().month + 1
		}
		
		def doctorInfo = doctorInfoRepository.findOne(doctorId),
			bookingTime, data = []
		if (doctorInfo)
			bookingTime = doctorInfo.bookingTime
		def times = bookingTime.split(',')
		
		def strYear = DateFormatUtils.format(new Date(), 'yyyy'),
			year = DateUtils.parseDate("$strYear-$month", 'yyyy-MM')
		
//		def time = 
//		def bookingTimeList = doctorBookingTimeRepository.findAll(
//			doctor_equal : new Doctor(id : doctorId),
//			startTime_greaterThanOrEqualTo : year,
//			endTime_lessThanOrEqualTo : year
//		)
//		def bookingTimeList = doctorBookingTimeRepository.findAllByTime(year, new Doctor(id : doctorId))
		
		def c = Calendar.getInstance()
		c.setTime year
		def maxDay = c.getMaximum(Calendar.DAY_OF_MONTH)
		
		for (i in 1..maxDay) {
			def day = DateUtils.parseDate("$strYear-$month-$i", 'yyyy-MM-dd'),
				idx = dayForWeek(day),
				timeModel = times[idx - 1],
				timeList = bookingTimeRepository.findAllByTime(DateFormatUtils.format(day, 'yyyy-MM-dd'), new Doctor(id : doctorId)),
				bookingList = bookingRepository.findAllByTime(DateFormatUtils.format(day, 'yyyy-MM-dd'), new Doctor(id : doctorId))
			
			timeList.each {
				def start = Integer.valueOf(DateFormatUtils.format(it.startTime, 'HH')),
					end
				if (DateUtils.isSameDay(it.startTime, it.endTime)) {
					end = Integer.valueOf(DateFormatUtils.format(it.endTime, 'HH'))
				} else {
					end = 24
				}
					
				for (j in start .. end - 1) {
					def occupy = timeModel.substring(j, j + 1)
					// 1 占用， 0 未占用
					if ("1" != occupy || !"1".equals(occupy)) {
						timeModel = timeModel.substring(0, j) + '1' + timeModel.substring(j + 1)
					}
				}
			}
			
			bookingList.each {
				def index = Integer.valueOf(DateFormatUtils.format(it.time, 'HH')),
					occupy = timeModel.substring(index, index + 1)
				if ("1" != occupy || !"1".equals(occupy)) {
					timeModel = timeModel.substring(0, index) + '1' + timeModel.substring(index + 1)
				}
			}
			data << [
				'time' : DateFormatUtils.format(day, 'yyyy-MM-dd'),
				'timeModel' : timeModel
			]
		}
		[
			'success' : '1',
			'data' : data
		]
	}
	
	/**
	 * @param bookingId
	 * @param time
	 * @param user
	 * @return
	 * 提交时间
	 */
	@RequestMapping(value = 'submitBookingTime', method = RequestMethod.POST)
	submitBookingTime(@RequestParam Long bookingId, @RequestParam String time, @ModelAttribute('currentUser') User user) {
		
		logger.trace '-- 提交预约时间 --'
		
		def booking = bookingRepository.findOne(bookingId)
		if (time)
			booking.time = DateUtils.parseDate(time, 'yyyy-MM-dd HH')
		
		bookingRepository.save(booking)
		'{"success" : "1"}'
	}
	
	def dayForWeek(Date date) {
		def c = Calendar.getInstance()
		c.setTime date
		def idx
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			idx = 7
		} else {
			idx = c.get(Calendar.DAY_OF_WEEK) - 1
		}
		idx
	}

}