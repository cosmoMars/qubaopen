package com.qubaopen.doctor.controller.booking;

import java.awt.event.ItemEvent;

import org.apache.commons.lang3.time.DateFormatUtils
import org.apache.commons.lang3.time.DateUtils
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
import com.qubaopen.doctor.repository.booking.BookingTimeRepository;
import com.qubaopen.doctor.repository.doctor.BookingRepository
import com.qubaopen.doctor.repository.doctor.DoctorInfoRepository
import com.qubaopen.survey.entity.booking.Booking
import com.qubaopen.survey.entity.doctor.Doctor
import com.qubaopen.survey.entity.user.User

@RestController
@RequestMapping('booking')
@SessionAttributes('currentDoctor')
public class BookingController extends AbstractBaseController<Booking, Long> {

	private static Logger logger = LoggerFactory.getLogger(BookingController.class)
	
	@Autowired
	BookingRepository bookingRepository
	
	@Autowired
	DoctorInfoRepository doctorInfoRepository
	
	@Autowired
	BookingTimeRepository doctorBookingTimeRepository
	
	@Override
	protected MyRepository<Booking, Long> getRepository() {
		return bookingRepository;
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
				bookingList = bookingRepository.findBookingList(doctor, status, age, ids, pageable)
			} else {
				bookingList = bookingRepository.findBookingList(doctor, status, age, pageable)
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
				bookingList = bookingRepository.findBookingList(doctor, age, ids, pageable)
			} else {
				bookingList = bookingRepository.findBookingList(doctor, age, pageable)
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
		
		def booking = bookingRepository.findOne(id)
		
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
			bookingList = bookingRepository.findByUserAndStatus(doctor, new User(id : userId), Booking.Status.Consulted, ids, pageable)
		} else {
			bookingList = bookingRepository.findByUserAndStatus(doctor, new User(id : userId), Booking.Status.Consulted, pageable)
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
//		def bookingList = doctorBookingRepository.retrieveBookingByMonth(doctor, month)		
//		
//		def data = []
//		bookingList.each {
//			data << [
//				'time' : DateFormatUtils.format(it.time, 'yyyy-MM-dd HH'),
//				'consultType' : it?.consultType?.ordinal()
//			]
//		}
//		[
//			'success' : '1',
//			'data' : data
//		]
		def doctorInfo = doctorInfoRepository.findOne(doctor.id),
			bookingTime = doctorInfo.bookingTime,
			data = []
		def times = bookingTime.split(',')
		
		def strYear = DateFormatUtils.format(new Date(), 'yyyy'),
			year = DateUtils.parseDate("$strYear-$month", 'yyyy-MM')
		def c = Calendar.getInstance()
		c.setTime year
		def maxDay = c.getMaximum(Calendar.DAY_OF_MONTH)
		
		for (i in 1..maxDay) {
			def day = DateUtils.parseDate("$strYear-$month-$i", 'yyyy-MM-dd'),
				idx = dayForWeek(day),
				timeModel = times[idx - 1],
				timeList = doctorBookingTimeRepository.findAllByTime(DateFormatUtils.format(day, 'yyyy-MM-dd'), doctor),
				todayData = []
			timeList.each {
				todayData << [
					'timeId' : it?.id,
					'startTime' : it?.startTime,
					'endTime' : it?.endTime,
					'userId' : it?.user?.id,
					'userName' : it?.booking?.name,
					'location' : it?.location,
					'content' : it?.content,
					'remindTime' : it?.remindTime
				]
				
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
			data << [
				'time' : DateFormatUtils.format(day, 'yyyy-MM-dd'),
				'timeModel' : timeModel,
				'todayData' : todayData
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
		
		def booking = bookingRepository.findOne(id),
			bookingStatus = Booking.Status.values()[index]
		
		booking.status = bookingStatus
		
		if (bookingStatus && bookingStatus == Booking.Status.Next) {
			Calendar cal = Calendar.getInstance()
			cal.setTime(booking.time)
			
			cal.add(Calendar.DATE, 7)
			def newTime = cal.getTime()
			booking.time = newTime
		}
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
