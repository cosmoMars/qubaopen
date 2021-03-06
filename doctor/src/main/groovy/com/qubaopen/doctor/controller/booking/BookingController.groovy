package com.qubaopen.doctor.controller.booking
import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.doctor.repository.booking.BookingSelfTimeRepository
import com.qubaopen.doctor.repository.booking.BookingTimeRepository
import com.qubaopen.doctor.repository.cash.DoctorCashLogRepository
import com.qubaopen.doctor.repository.cash.DoctorCashRepository
import com.qubaopen.doctor.repository.doctor.BookingRepository
import com.qubaopen.doctor.repository.doctor.DoctorInfoRepository
import com.qubaopen.doctor.repository.payEntity.PayEntityRepository
import com.qubaopen.doctor.repository.user.UserBookingDataRepository
import com.qubaopen.doctor.service.SmsService
import com.qubaopen.survey.entity.booking.Booking
import com.qubaopen.survey.entity.booking.ResolveType
import com.qubaopen.survey.entity.cash.DoctorCash
import com.qubaopen.survey.entity.cash.DoctorCashLog
import com.qubaopen.survey.entity.doctor.Doctor
import com.qubaopen.survey.entity.doctor.DoctorInfo
import com.qubaopen.survey.entity.user.User
import org.apache.commons.lang3.time.DateFormatUtils
import org.apache.commons.lang3.time.DateUtils
import org.joda.time.DateTime
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort.Direction
import org.springframework.data.web.PageableDefault
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

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
	BookingTimeRepository bookingTimeRepository
	
	@Autowired
	BookingSelfTimeRepository bookingSelfTimeRepository
	
	@Autowired
	PayEntityRepository payEntityRepository

	@Autowired
	DoctorCashRepository doctorCashRepository

	@Autowired
	DoctorCashLogRepository doctorCashLogRepository

    @Autowired
    SmsService smsService

	@Autowired
	UserBookingDataRepository userBookingDataRepository

	@Value('${ratio}')
	double ratio
	
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
		def bookingList, nowDate = new Date(), ago
//		def c = Calendar.getInstance()
//		c.setTime(nowDate)
//		c.add(Calendar.DATE, -7)
//		ago = c.getTime() as Date

		def result = bookingRepository.countBooking(doctor)

		if (index == null || index == 0) {
			if (idsStr) {
				bookingList = bookingRepository.findBookingList(doctor, ids, pageable)
			} else {
//				bookingList = bookingRepository.findBookingList(doctor, pageable)
				bookingList = bookingRepository.findAll(
						[
								doctor_equal: doctor,
								status_notEqual: Booking.Status.Close
						], pageable
				)
			}
		} else {
			def idsStatus = []

			if (index == 2) {
//				idsStatus = [Booking.Status.PayAccept]

				if (idsStr) {
					bookingList = bookingRepository.findByWithoutStatusAndNullDoctorStatus(doctor, ids, pageable)
				} else {
					bookingList = bookingRepository.findByWithoutStatusAndNullDoctorStatus(doctor, pageable)
				}
			} else {
				if (index == 1) {
					idsStatus = [Booking.Status.Booking, Booking.Status.Paid]
				} else if (index == 3) {
					idsStatus = [Booking.Status.Consulted, Booking.Status.Next]
				} else if (index == 4) {
					idsStatus = [Booking.Status.ChangeDate]
				}
				if (idsStr) {
					bookingList = bookingRepository.findBookingListWithoutStatus(doctor, idsStatus, ids, pageable)
				} else {
					bookingList = bookingRepository.findBookingListWithoutStatus(doctor, idsStatus, pageable)
				}
			}

		}

		def data = []
		bookingList.each {
			def money = it?.money + it?.quickMoney
			data << [
				'bookingId' : it?.id,
				'userId' : it.user?.id,
				'userName' : it?.name,
				'helpReason' : it?.helpReason,
				'refusalReason' : it?.refusalReason,
				'time' : it?.time,
				'quick' : it?.quick,
				'consultType' : it?.consultType?.ordinal(),
				'status' : it?.status?.ordinal(),
				'money' : money,
				'userStatus'  : it?.userStatus?.ordinal(),
				'doctorStatus': it?.doctorStatus?.ordinal(),
				'userSex' : it?.sex?.ordinal(),
				'avatar' : it?.user?.userInfo?.avatarPath
			]
		}
		def more = true
		if (data.size() < pageable.pageSize) {
			more = false
		}
		[
			'success' : '1',
			'data' : data,
			'all'      : result['all'],
			'toConfirm': result['toConfirm'],
			'toConsult': result['toConsult'],
			'record'   : result['record'],
			'change'   : result['change'],
			'more' : more
		]
	}
	
	/**
	 * @param id
	 * @return
	 * 获取详细信息
	 */
	@RequestMapping(value = 'retrieveDetailInfo/{id}', method = RequestMethod.GET)
	retrieveDetailInfo(@PathVariable('id') Long id, @ModelAttribute('currentDoctor') Doctor doctor) {
		
		logger.trace('-- 查看单个信息 --')
		
		def booking = bookingRepository.findOne(id)

		if (booking) {
			[
				'success' : '1',
				'userId' : booking?.user?.id,
				'userName' : booking?.name,
				'phone' : booking?.phone,
				'userSex' : booking?.sex?.ordinal(),
				'birthday' : booking?.birthday,
				'profession' : booking?.profession,
				'city' : booking?.city,
				'married' : booking?.married,
				'haveChildren' : booking?.haveChildren,
				'helpReason' : booking?.helpReason,
				'otherProblem' : booking?.otherProblem,
				'treatmented' : booking?.treatmented,
				'haveConsulted' : booking?.haveConsulted,
				'userStatus' : booking?.userStatus,
				'doctorStatus' : booking?.doctorStatus
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
		def data = []
		bookingList.each {
			data << [
				'bookingId' : it?.id,
				'userId' : it?.user?.id,
				'doctorName' : it?.doctor?.doctorInfo?.name,
				'userName' : it?.name,
				'helpReason' : it?.helpReason,
				'refusalReason' : it?.refusalReason,
				'time' : it?.time,
				'quick' : it?.quick,
				'consultType' : it?.consultType?.ordinal(),
				'status' : it?.status?.ordinal(),
				'money' : it?.money,
				'userStatus' : it?.userStatus,
				'doctorStatus' : it?.doctorStatus,
				'doctorAvatar' : it?.doctor?.doctorInfo?.avatarPath
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
	retrieveBookingList(@RequestParam(required = false) String time, @ModelAttribute('currentDoctor') Doctor doctor) {

		logger.trace('-- 订单日历 --')
		
		def date
		
		if (time == null) {
			date = new Date()
		} else {
			date = DateUtils.parseDate(time, 'yyyy-MM-dd')
//			def today = DateUtils.parseDate(DateFormatUtils.format(new Date(), 'yyyy-MM-dd'), 'yyyy-MM-dd')
//			if (date < today) {
//				return '{"success" : "0", "message" : "err801"}'
//			}
		}
		def doctorInfo = doctorInfoRepository.findOne(doctor.id),
			bookingTime, data = []
		if (doctorInfo)
			bookingTime = doctorInfo.bookingTime
		def times = bookingTime.split(','),
			c = Calendar.getInstance()
		c.setTime date
		
		def dayData = [], timeData = []
		
		for (i in 0..6) {
			if (i > 0)
				c.add(Calendar.DATE, 1)
			def day = c.getTime(),
				idx = dayForWeek(day),
				timeModel = times[idx - 1],
				bTime = bookingTimeRepository.findByFormatTime(doctor, DateFormatUtils.format(day, 'yyyy-MM-dd')),
				bookingSelfTimes = bookingSelfTimeRepository.findByDoctorAndTime(doctor, day),
//				timeList = bookingTimeRepository.findAllByTime(DateFormatUtils.format(day, 'yyyy-MM-dd'), doctor),
				bookingList = bookingRepository.findAllByTimeAndDoctor(DateFormatUtils.format(day, 'yyyy-MM-dd'), doctor),
				self = [], other = []
				
			dayData << [
				'dayId' : i + 1,
				'dayOfWeek' : idx,
				'day' : DateFormatUtils.format(c.getTime(), 'yyyy-MM-dd')
			]
			
			if (bTime) {
				timeModel = bTime.bookingModel // default 3
			}
			if (bookingSelfTimes && bookingSelfTimes.size() > 0) { 
				bookingSelfTimes.each {
					def start, end
					if (DateUtils.isSameDay(it.startTime, day)) {
						start = Integer.valueOf(DateFormatUtils.format(it.startTime, 'HH'))
						if (DateUtils.isSameDay(it.startTime, it.endTime)) {
							end = Integer.valueOf(DateFormatUtils.format(it.endTime, 'HH'))
						} else {
							end = 23
						}
					} else if (!DateUtils.isSameDay(it.startTime, day) && it.startTime.before(day)) {
						start = 0
						end = Integer.valueOf(DateFormatUtils.format(it.endTime, 'HH'))
					}
					for (j in start .. end) {
						def occupy = timeModel.substring(j, j + 1)
						// 1 占用， 0 未占用
						if ("1" != occupy || !"1".equals(occupy)) {
							timeModel = timeModel.substring(0, j) + '1' + timeModel.substring(j + 1)
						}
					}
					self << [
						'timeId' : it.id,
						'startTime' : it?.startTime,
						'endTime' : it?.endTime,
						'location' : it?.location,
						'content' : it?.content
					]
				}
			}
			bookingList.each {
				def index = Integer.valueOf(DateFormatUtils.format(it.time, 'HH')),
					occupy = timeModel.substring(index, index + 1)
				if ("1" != occupy || !"1".equals(occupy)) {
					timeModel = timeModel.substring(0, index) + '1' + timeModel.substring(index + 1)
				}
				
				def endTime = ''
				if (it.time) {
					endTime = "${DateFormatUtils.format(it.time, 'yyyy-MM-dd')} ${index + 1}:00:00" as String
				}
				other << [
					'bookingId' : it?.id,
					'name' : it?.name,
					'helpReason' : it?.helpReason,
					'consultType' : it?.consultType?.ordinal(),
					'startTime' : it?.time,
					'endTime' : endTime
				]
			}
			def timeAll = []
			for (k in 0..timeModel.length() - 1) {
				if ('0' == timeModel[k] || '0'.equals(timeModel[k])) {
					timeAll << [
						'dayId': i + 1,
						'startTime' : DateFormatUtils.format(DateUtils.parseDate("$k:00", 'HH:mm'), 'HH:mm'),
						'endTime' : DateFormatUtils.format(DateUtils.parseDate("${k+1}:00", 'HH:mm'), 'HH:mm')
					]
				}
			}
			timeData << [
				'time' : timeAll,
				'self' : self,
				'other' : other
	        ]
		}
		[
			'success' : '1',
			'dayData' : dayData,
			'timeData' : timeData
			
		]
	}
	/**
	 * @param month
	 * @param doctor
	 * @return
	 * 日历列表
	 */
	@RequestMapping(value = 'retrieveWeeklySchedule', method = RequestMethod.GET)
	retrieveWeeklySchedule(@RequestParam(required = false) String time, @ModelAttribute('currentDoctor') Doctor doctor) {

		logger.trace('-- 订单日历 --')
		
		def date
		
		if (time == null) {
			date = new Date()
		} else {
			date = DateUtils.parseDate(time, 'yyyy-MM-dd')
//			def today = DateUtils.parseDate(DateFormatUtils.format(new Date(), 'yyyy-MM-dd'), 'yyyy-MM-dd')
//			if (date < today) {
//				return '{"success" : "0", "message" : "err801"}'
//			}
		}
		def doctorInfo = doctorInfoRepository.findOne(doctor.id),
			bookingTime, data = []
		if (doctorInfo)
			bookingTime = doctorInfo.bookingTime
		def times = bookingTime.split(',')
		def c = Calendar.getInstance()
		c.setTime date
		
		def result = []
		for (i in 0..6) {
			def dayData = [], timeData = []
			if (i > 0)
				c.add(Calendar.DATE, 1)
			def day = c.getTime(),
				idx = dayForWeek(day),
				timeModel = times[idx - 1],
				bTime = bookingTimeRepository.findByFormatTime(doctor, DateFormatUtils.format(day, 'yyyy-MM-dd')),
				bookingSelfTimes = bookingSelfTimeRepository.findByDoctorAndTime(doctor, day),
//				timeList = bookingTimeRepository.findAllByTime(DateFormatUtils.format(day, 'yyyy-MM-dd'), doctor),
				bookingList = bookingRepository.findAllByTimeAndDoctor(DateFormatUtils.format(day, 'yyyy-MM-dd'), doctor),
//				self = [], other = []
				timeAll = []
				
			if (bTime) {
				timeModel = bTime.bookingModel // default 3
			}
			
			for (j in 0..timeModel.length() - 1) {
				if ('1' == timeModel[j] || '1'.equals(timeModel[j])) {
					timeAll << [
						'type' : 3,
						'startTime' : DateFormatUtils.format(DateUtils.parseDate("$j:00", 'HH:mm'), 'HH:mm'),
						'endTime' : DateFormatUtils.format(DateUtils.parseDate("${j+1}:00", 'HH:mm'), 'HH:mm')
					]
				}
			}
			
			if (bookingSelfTimes && bookingSelfTimes.size() > 0) {
				bookingSelfTimes.each {
					
					if (DateUtils.isSameDay(it.startTime, day)) {
						if (DateUtils.isSameDay(it.startTime, it.endTime)) {
							timeAll << [
								'type' : 1,
								'timeId' : it.id,
								'startTime' : DateFormatUtils.format(it?.startTime, 'HH:mm'),
								'endTime' : DateFormatUtils.format(it?.endTime, 'HH:mm'),
								'location' : it?.location,
								'content' : it?.content
							]
						} else {
							timeAll << [
								'type' : 1,
								'timeId' : it.id,
								'startTime' : DateFormatUtils.format(it?.startTime, 'HH:mm'),
								'endTime' : '00:00',
								'location' : it?.location,
								'content' : it?.content
							]
						}
					} else if (!DateUtils.isSameDay(it.startTime, day) && it.startTime.before(day)) {
						if (DateUtils.isSameDay(it.endTime, day)) {
							timeAll << [
								'type' : 1,
								'timeId' : it.id,
								'startTime' : '00:00',
								'endTime' : DateFormatUtils.format(it?.endTime, 'HH:mm'),
								'location' : it?.location,
								'content' : it?.content
							]
						} else {
							timeAll << [
								'type' : 1,
								'timeId' : it.id,
								'startTime' : '00:00',
								'endTime' : '24:00',
								'location' : it?.location,
								'content' : it?.content
							]
						}
					}
				}
			}
			bookingList.each {
				def cal = Calendar.getInstance()
				cal.setTime it?.time
				cal.add(Calendar.HOUR, 1)
				def endTime = cal.getTime()
				timeAll << [
					'type' : 2,
					'bookingId' : it?.id,
					'name' : it?.name,
					'helpReason' : it?.helpReason,
					'consultType' : it?.consultType?.ordinal(),
					'startTime' : DateFormatUtils.format(it?.time, 'HH:mm'),
					'endTime' : DateFormatUtils.format(endTime, 'HH:mm'),
					'quick' : it?.quick,
					'status' : it?.status?.ordinal()
				]
			}
			
			result << [
				'dayOfWeek' : idx,
				'day' : DateFormatUtils.format(c.getTime(), 'yyyy-MM-dd'),
				'timeData' : timeAll
			]
			
		}
		[
			'success' : '1',
			'result' : result
		]
	}
	
	/**
	 * @param id
	 * @param index
	 * @param doctor
	 * @return
	 * 修改订单状态
	 */
	@Transactional
	@RequestMapping(value = 'modifyBookingStatus', method = RequestMethod.POST)
	modifyBookingStatus(@RequestParam(required = false) Long id,
		@RequestParam(required = false) String content,
		@RequestParam(required = false) Integer index,
		@ModelAttribute('currentDoctor') Doctor doctor) {
		
		logger.trace('-- 修改订单状态 --')
		
		def di = doctorInfoRepository.findOne(doctor.id)
		
		if (di.loginStatus != DoctorInfo.LoginStatus.Audited) {
			return '{"success" : "0", "message" : "err916"}'
		}
	
		def booking = bookingRepository.findOne(id),
			bookingStatus = Booking.Status.values()[index]


        if (bookingStatus == Booking.Status.Accept) {
            String param1 = '{"url" : "http://zhixin.me/smsRedirect.html"}'
            smsService.sendSmsMessage(booking.getPhone(), 5, param1)
        }
		booking.status = bookingStatus
		
		if (bookingStatus && bookingStatus == Booking.Status.Refusal) {
			booking.refusalReason = content

		}
		
		if (bookingStatus && bookingStatus == Booking.Status.Next) {
			Calendar cal = Calendar.getInstance()
			cal.setTime(booking.time)
			
			cal.add(Calendar.DATE, 7)
			def newTime = cal.getTime()
			booking.time = newTime
		}


        booking.lastModifiedDate = new DateTime()

        booking.resolveType = ResolveType.None
        booking.sendEmail = false

		// 查找修改信息
		def userBookingData = userBookingDataRepository.findOneByFilters(
				[
						user_equal   : booking.user,
						booking_equal: booking
				]
		)
		userBookingData.thirdRefresh = true

		userBookingDataRepository.save(userBookingData)
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
	
	/************************************************************************************************************************/
	
	/**
	 * @param qBookingId
	 * @param doctor
	 * @return
	 * 获取冲突订单
	 */
	@RequestMapping(value = 'retrieveChangeDateBooking', method = RequestMethod.POST)
	retrieveChangeDateBooking(@RequestParam long qBookingId,
		@ModelAttribute('currentDoctor') Doctor doctor) {

		def qBooking = bookingRepository.findOne(qBookingId)
		if (!qBooking?.time) {
			return '{"success" : "1"}'
		}
		def aBookings = bookingRepository.findAllByFormatTimeAndQuick(DateFormatUtils.format(qBooking?.time, 'yyyy-MM-dd HH'), doctor, true, qBookingId)
		
		if (aBookings) {
			return '{"success" : "0", "message" : "err805"}'
		}
		def fbookings = bookingRepository.findAllByFormatTimeAndQuick(DateFormatUtils.format(qBooking?.time, 'yyyy-MM-dd HH'), doctor, false, qBookingId),
		data = [], now = System.currentTimeMillis()
		
		fbookings.each {
			if (it?.time?.time - now < 4 * 60 * 60 * 1000) {
				return '{"success" : "0", "message" : "err806"}'
			}
			if (it?.status == Booking.Status.ChangeDate) {
				return  '{"success" : "0", "message" : "err918"}'
			}
			
			data << [
				'id' : it.id
			]
		}
		[
			'success' : '1',
			'data' : data
		]
	}
		
	
	/**
	 * @param id
	 * @param date
	 * @param doctor
	 * @return
	 * 改约时间
	 */
	@RequestMapping(value = 'changeDateForBooking', method = RequestMethod.POST)
	changeDateForBooking(@RequestParam(required = false) Long id,
		@RequestParam(required = false) String date,
		@ModelAttribute('currentDoctor') Doctor doctor) {
		
		def di = doctorInfoRepository.findOne(doctor.id)
		
		if (di.loginStatus != DoctorInfo.LoginStatus.Audited) {
			return '{"success" : "0", "message" : "err916"}'
		}
		
		def booking = bookingRepository.findOne(id)

		if (booking.status != Booking.Status.Paid && booking.quick) {
			return '{"success" : "0", "message" : "err812"}'
		}
		
		if (date){
			booking.time = DateUtils.parseDate(date, 'yyyy-MM-dd HH')
			booking.status = Booking.Status.ChangeDate
		}
		// 查找修改信息
		def userBookingData = userBookingDataRepository.findOneByFilters(
				[
						user_equal   : booking.user,
						booking_equal: booking
				]
		)
		userBookingData.thirdRefresh = true

		userBookingDataRepository.save(userBookingData)

		bookingRepository.save(booking)
		'{"success" : "1"}'
	}
		
	/**
	 * @param bookingId
	 * @param idx
	 * @param doctor
	 * @return
	 * 修改医师订单状态
	 */
	@Transactional
	@RequestMapping(value = 'confirmDoctorBookingStatus', method = RequestMethod.POST)
	confirmDoctorBookingStatus(@RequestParam long bookingId,
		@RequestParam(required = false) Integer idx,
		@ModelAttribute('currentDoctor') Doctor doctor) {
		
		logger.trace '--- 修改医师订单状态 ---'
		
		def di = doctorInfoRepository.findOne(doctor.id)
		
		if (di.loginStatus != DoctorInfo.LoginStatus.Audited) {
			return '{"success" : "0", "message" : "err916"}'
		}
	
		def booking = bookingRepository.findOne(bookingId)
		
		if (idx != null) {
			booking.doctorStatus = Booking.BookStatus.values()[idx]
		}
		if (booking.doctorStatus == Booking.BookStatus.Consulted && booking.userStatus == Booking.BookStatus.Consulted) {
			booking.status = Booking.Status.Consulted

			def cash = booking.money * ratio as Double
			def doctorCash = doctorCashRepository.findOne(booking.doctor.id)
			if (doctorCash) {
				doctorCash.inCash += cash
				doctorCash.currentCash += cash
			} else {
				doctorCash = new DoctorCash(
						doctor: booking.doctor,
						inCash: cash,
						currentCash: cash
				)
			}
			doctorCashRepository.save(doctorCash)
			def doctorCashLog = new DoctorCashLog(
					doctor: booking.doctor,
					user: booking.user,
					userName: booking.name,
					time: new Date(),
					type: DoctorCashLog.Type.In,
					payStatus: DoctorCashLog.PayStatus.Completed,
					cash: cash
			)
			doctorCashLogRepository.save(doctorCashLog)
		}
		booking.resolveType = ResolveType.None
		booking.sendEmail = false

		// 查找修改信息
		def userBookingData = userBookingDataRepository.findOneByFilters(
				[
						user_equal   : booking.user,
						booking_equal: booking
				]
		)
		userBookingData.thirdRefresh = true

		userBookingDataRepository.save(userBookingData)
		bookingRepository.save(booking)
		'{"success" : "1"}'
	}
		
}
