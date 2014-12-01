package com.qubaopen.doctor.controller.doctor;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort.Direction
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttributes

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.doctor.repository.doctor.DoctorBookingRepository;
import com.qubaopen.survey.entity.booking.Booking;
import com.qubaopen.survey.entity.doctor.Doctor
import com.qubaopen.survey.entity.user.User

@RestController
@RequestMapping('doctorBooking')
@SessionAttributes('currentDoctor')
public class DoctorBookingController extends AbstractBaseController<Booking, Long> {

	private static Logger logger = LoggerFactory.getLogger(DoctorBookingController.class)
	
	@Autowired
	DoctorBookingRepository doctorBookingRepository
	
	@Override
	protected MyRepository<Booking, Long> getRepository() {
		return doctorBookingRepository;
	}

	/**
	 * @param doctor
	 * @return
	 * 获取预约信息
	 */
	@RequestMapping(value = 'retrieveBookingInfo', method = RequestMethod.POST)
	retrieveBookingInfo(@RequestParam(required = false) Integer index,
		@RequestParam(required = false) String idsStr,
		@PageableDefault(page = 0, size = 20, sort = 'createdDate', direction = Direction.DESC)
		Pageable pageable,
		@ModelAttribute('currentDoctor') Doctor doctor) {
		
		logger.trace('-- 获取预约信息 --')
		
		def ids = []
		if (idsStr) {
			def tempIds = idsStr.split(',')
			tempIds.each {
				ids << Long.valueOf(it.trim())
			}
		}
		def bookingList, nowDate = new Date(), age
		def c = Calendar.getInstance()
		c.setTime(nowDate)
		c.add(Calendar.DATE, -7)
		age = c.getTime() as Date
		
		if (index != null) {
			def status = Booking.Status.values()[index]
			if (idsStr) {
				bookingList = doctorBookingRepository.findDoctorBookingList(doctor, status, age, ids, pageable)
			} else {
				bookingList = doctorBookingRepository.findDoctorBookingList(doctor, status, age, pageable)
			}
//			bookingList = doctorBookingRepository.findAll(
//				[
//					doctor_equal : doctor,
//					status_equal : status,
//					time_greaterThanOrEqualTo : age
//				], pageable
//			)
		} else {
			if (idsStr) {
				bookingList = doctorBookingRepository.findDoctorBookingList(doctor, age, ids, pageable)
			} else {
				bookingList = doctorBookingRepository.findDoctorBookingList(doctor, age, pageable)
			}
//			bookingList = doctorBookingRepository.findAll(
//				[
//					doctor_equal : doctor,
//					time_greaterThanOrEqualTo : age
//				], pageable
//			
//			)
		}
		
		def data = []
		bookingList.each {
			data << [
				'bookingId' : it.id,
				'userId' : it.user?.id,
				'userName' : it?.name,
				'helpReason' : it.refusalReason,
				'refusalReason' : it.refusalReason,
				'time' : it.time,
				'quick' : it.quick,
				'consultType' : it.consultType?.ordinal(),
				'status' : it.status?.ordinal(),
				'money' : it.money
			]
		}
		def more = true
		if (bookingList.size() < pageable.pageSize) {
			more = false
		}
		[
			'success' : '1',
			'data' : data,
			'more' : more
		]
	}
	
	/**
	 * @param id
	 * @return
	 * 获取详细信息
	 */
	@RequestMapping(value = 'retrieveDetailInfo/{id}', method = RequestMethod.GET)
	retrieveDetailInfo(@PathVariable('id') Long id) {
		
		logger.trace('-- 查看单个信息 --')
		
		def booking = doctorBookingRepository.findOne(id)
		
		if (booking) {
			[
				'success' : '1',
				'userId' : booking?.user?.id,
				'userName' : booking?.name,
				'userSex' : booking?.sex?.ordinal(),
				'birthday' : booking?.birthday,
				'profession' : booking?.profession,
				'city' : booking?.city,
				'married' : booking?.married,
				'haveChildren' : booking?.haveChildren,
				'helpReason' : booking?.helpReason,
				'otherProblem' : booking?.otherProblem,
				'treatmented' : booking?.treatmented,
				'haveConsulted' : booking?.haveConsulted
			]
		} else {
			'{"success" : "0"}'
		}
	}
	
	/**
	 * @param userId
	 * @param idsStr
	 * @param pageable
	 * @param doctor
	 * @return
	 * 
	 * 获取历史
	 */
	@RequestMapping(value = 'retrieveHistory', method = RequestMethod.POST)
	retrieveHistory(@RequestParam(required = false) Long userId,
		@RequestParam(required = false) String idsStr,
		@PageableDefault(page = 0, size = 20, sort = 'createdDate', direction = Direction.ASC)
		Pageable pageable,
		@ModelAttribute('currentDoctor') Doctor doctor) {
		
		logger.trace('-- 查询历史 --')
		
		def ids = [], bookingList
		if (idsStr) {
			def tempIds = idsStr.split(',')
			tempIds.each {
				ids << Long.valueOf(it.trim())
			}
		}
		if (idsStr) {
			bookingList = doctorBookingRepository.findByUserAndStatus(doctor, new User(id : userId), Booking.Status.Consulted, ids, pageable)
		} else {
			bookingList = doctorBookingRepository.findByUserAndStatus(doctor, new User(id : userId), Booking.Status.Consulted, pageable)
		}
		
//		bookingList = doctorBookingRepository.findAll(
//			[
//				doctor_equal : doctor,
//				user_equal : new User(id : userId),
//				status_equal : DoctorBooking.Status.Consulted
//			], pageable
//		)
		def data = []
		bookingList.each {
			data << [
				'userId' : it.user?.id,
				'userName' : it?.name,
				'helpReason' : it.refusalReason,
				'refusalReason' : it.refusalReason,
				'time' : it.time,
				'quick' : it.quick,
				'consultType' : it.consultType?.ordinal(),
				'status' : it.status?.ordinal(),
				'money' : it.money
			]
		}
		def more = true
		if (bookingList.size() < pageable.pageSize) {
			more = false
		}
		[
			'success' : '1',
			'data' : data,
			'more' : more
		]
	}
	
		
	/**
	 * @param month
	 * @param doctor
	 * @return
	 * 日历列表
	 */
	@RequestMapping(value = 'retrieveBookingList', method = RequestMethod.GET)
	retrieveBookingList(@RequestParam(required = false) Integer month, @ModelAttribute('currentDoctor') Doctor doctor) {

		logger.trace('-- 订单日历 --')
		
		if (month == null) {
			month = new Date().getMonth() + 1
		}
		def bookingList = doctorBookingRepository.retrieveBookingByMonth(doctor, month)		
		
		def data = []
		bookingList.each {
			data << [
				'time' : DateFormatUtils.format(it.time, 'yyyy-MM-dd HH'),
				'consultType' : it?.consultType?.ordinal()
			]
		}
		[
			'success' : '1',
			'data' : data
		]
	}
	
	
	/**
	 * @param id
	 * @param index
	 * @param doctor
	 * @return
	 * 修改订单状态
	 */
	@RequestMapping(value = 'modifyBookingStatus', method = RequestMethod.POST)
	modifyBookingStatus(@RequestParam(required = false) Long id, @RequestParam(required = false) Integer index, @ModelAttribute('currentDoctor') Doctor doctor) {
		
		logger.trace('-- 修改订单状态 --')
		
		def booking = doctorBookingRepository.findOne(id),
			bookingStatus = Booking.Status.values()[index]
		
		booking.status = bookingStatus
		
		if (bookingStatus && bookingStatus == Booking.Status.Next) {
			Calendar cal = Calendar.getInstance()
			cal.setTime(booking.time)
			
			cal.add(Calendar.DATE, 7)
			def newTime = cal.getTime()
			booking.time = newTime
		}
		doctorBookingRepository.save(booking)
		'{"success" : "1"}'
	}
	
}
