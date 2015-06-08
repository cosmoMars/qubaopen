package com.knowheart3.controller.booking
import com.knowheart3.repository.booking.BookingRepository
import com.knowheart3.repository.booking.BookingSelfTimeRepository
import com.knowheart3.repository.booking.BookingTimeRepository
import com.knowheart3.repository.doctor.DoctorCashLogRepository
import com.knowheart3.repository.doctor.DoctorCashRepository
import com.knowheart3.repository.doctor.DoctorInfoRepository
import com.knowheart3.repository.doctor.DoctorRepository
import com.knowheart3.repository.hospital.HospitalRepository
import com.knowheart3.repository.user.UserAuthorizationRepository
import com.knowheart3.repository.user.UserBookingDataRepository
import com.knowheart3.service.SmsService
import com.knowheart3.service.booking.BookingService
import com.knowheart3.utils.CommonEmail
import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.booking.Booking
import com.qubaopen.survey.entity.booking.ResolveType
import com.qubaopen.survey.entity.cash.DoctorCash
import com.qubaopen.survey.entity.cash.DoctorCashLog
import com.qubaopen.survey.entity.doctor.Doctor
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.user.UserAuthorization
import com.qubaopen.survey.entity.user.UserBookingData
import org.apache.commons.lang3.time.DateFormatUtils
import org.apache.commons.lang3.time.DateUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort.Direction
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

import static com.knowheart3.utils.ValidateUtil.validatePhone
/**
 * @author mars
 *
 */
@RestController
@RequestMapping('booking')
@SessionAttributes('currentUser')
public class BookingController extends AbstractBaseController<Booking, Long> {

	@Autowired
	BookingRepository bookingRepository
	
	@Autowired
	DoctorInfoRepository doctorInfoRepository

	@Autowired
	DoctorRepository doctorRepository

	@Autowired
	HospitalRepository hospitalRepository
	
	@Autowired
	BookingTimeRepository bookingTimeRepository
	
	@Autowired
	BookingSelfTimeRepository bookingSelfTimeRepository

	@Autowired
	DoctorCashRepository doctorCashRepository

	@Autowired
	DoctorCashLogRepository doctorCashLogRepository

	@Autowired
	SmsService smsService

	@Autowired
	CommonEmail commonEmail

	@Autowired
	BookingService bookingService

	@Autowired
	UserAuthorizationRepository userAuthorizationRepository

	@Autowired
	UserBookingDataRepository userBookingDataRepository

	@Value('${ratio}')
	private double ratio
	
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
	@Transactional
	@RequestMapping(value = 'createBooking', method = RequestMethod.POST)
	createBooking(@RequestParam(required = false) Long doctorId,
				  @RequestParam(required = false) Long hospitalId,
				  @RequestParam(required = false) String name,
				  @RequestParam(required = false) String phone,
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
				  @RequestParam(required = false) Integer consultTypeIndex,
				  @RequestParam(required = false) Boolean authorise,
				  @ModelAttribute('currentUser') User user
	) {

		if (!user || !user.id) {
			return '{"success" : "0", "message": "err000"}'
		}

		if (!validatePhone(phone)) {
			return '{"success" : "0", "message": "err003"}'
		}
		if (!doctorId && !hospitalId) {
			return '{"success" : "0", "message" : "err810"}'
		}

		def tradeNo
		def sex, doctor, hospital
		if (doctorId != null) {
			doctor = doctorRepository.findOne(doctorId)

			// 查看用户对医师授权
			def ua = userAuthorizationRepository.findByUserAndDoctor(user, doctor)
			// 存在授权，取消授权
			if (ua && authorise == false) {
				userAuthorizationRepository.delete(ua)
			} else if (!ua && (authorise == null || authorise == true)) {
				ua = new UserAuthorization(
						user: user,
						doctor: doctor
				)
				userAuthorizationRepository.save(ua)
			}

			tradeNo = "u${user.id}d${doctorId}t${DateFormatUtils.format(new Date(), 'yyyyMMddHHmmssssss')}"
			/*def exist = bookingRepository.findAll([
                'user.id_equal' : user.id,
                'doctor.id_equal' : doctorId,
                'status_equal' : Booking.Status.Paying
            ])

            if (exist && exist.size() > 0) {
                return '{"success" : "0", "message" : "err809"}'
            }*/
		}
		if (hospitalId != null) {
			hospital = hospitalRepository.findOne(hospitalId)
			tradeNo = "u${user.id}h${hospitalId}t${DateFormatUtils.format(new Date(), 'yyyyMMddHHmmssssss')}"

//			def hospital = hospitalRepository.findOne(hospitalId)
//			if (!hospital) {
//				return '{"success" : "0", "message" : "没有该诊所"}'
//			}

			def exist = bookingRepository.findExistHospitalBooking(user.id, hospitalId)

			if (exist && exist.size() > 0) {
				return '{"success" : "0", "message" : "err809"}'
			}
		}


		def booking = new Booking(
				tradeNo: tradeNo,
				user: user,
				name: name,
				phone: phone,
				profession: profession,
				city: city,
				married: married,
				haveChildren: haveChildren,
				helpReason: helpReason,
				otherProblem: otherProblem,
				treatmented: treatmented,
				haveConsulted: haveConsulted,
				resolveType: ResolveType.None
//			time : new Date()
		)
		if (doctorId != null) {
			booking.doctor = doctor
		}
		if (hospitalId != null) {
			booking.hospital = hospital
		}
		if (birthdayStr) {
			booking.birthday = DateUtils.parseDate(birthdayStr, 'yyyy-MM-dd')
		}
		if (sexIndex != null) {
			sex = Booking.Sex.values()[sexIndex]
			booking.sex = sex
		}
		if (consultTypeIndex != null) {
			booking.consultType = Booking.ConsultType.values()[consultTypeIndex]

			if (doctorId != null) {
				def doctorInfo = doctorInfoRepository.findOne(doctorId)
				if (booking.consultType == Booking.ConsultType.Facetoface) {
					booking.money = doctorInfo.offlineFee
				} else if (booking.consultType == Booking.ConsultType.Video) {
					booking.money = doctorInfo.onlineFee
				}
			}
		}
		booking.status = Booking.Status.Booking
		booking = bookingRepository.save(booking)

		if (doctor) {
			def doctorPhone = doctor.doctorInfo?.phone
			if (doctorPhone) {
				def param = '{"url" : "http://zhixin.me/smsRedirectDr.html"}'
				smsService.sendSmsMessage(booking.doctor.doctorInfo.phone, 8, param)
			}
		}
		if (hospital) {
			commonEmail.sendTextMail(hospital.email)
		}

		def userBookingData = new UserBookingData(
				user: user,
				booking: booking
		)
		userBookingDataRepository.save(userBookingData)

		[
				'success'  : '1',
				'bookingId': booking?.id
		]
	}
		
	/**
	 * @param month
	 * @param doctorId
	 * @param user
	 * @return
	 * 用户获取医师每月列表
	 */
	@Transactional
	@RequestMapping(value = 'retrieveMonthBooking', method = RequestMethod.GET)
	retrieveMonthBooking(@RequestParam(required = false) String time,
		@RequestParam long doctorId,
		@RequestParam boolean quick,
		@ModelAttribute('currentUser') User user) {

		def now = new Date(), usedTime,
			date = DateUtils.parseDate(time, 'yyyy-MM-dd')
		def c = Calendar.getInstance()

		if (quick) {
			c.setTime now
			c.add(Calendar.HOUR_OF_DAY, 6)
			def sixLater = c.getTime()

			c.setTime now
			c.add(Calendar.DATE, 1)
			def oneDayLater = c.getTime()

			if (sixLater.compareTo(oneDayLater) < 1) {
				usedTime = sixLater
			} else {
				usedTime = oneDayLater
			}
		} else {
			c.setTime now
			c.add(Calendar.DATE, 1)
			c.set(Calendar.HOUR_OF_DAY, 0)
			c.set(Calendar.MINUTE, 0)
			usedTime = c.getTime()
		}

		def doctorInfo = doctorInfoRepository.findOne(doctorId),
			bookingTime, data = []
		if (doctorInfo)
			bookingTime = doctorInfo.bookingTime
		def times = bookingTime.split(',')

		
		def dayData = [], timeData = [], outDateBooking = []

		c.setTime date
		for (i in 0..6) {
			if (i > 0)
				c.add(Calendar.DATE, 1)
			
			def day = c.getTime(),
				idx = dayForWeek(day),
				timeModel = times[idx - 1] // 医师计划 1
				
			def dateBookingTime, selfBookingTimes, bookingList
			if (quick) {
				bookingList = bookingRepository.findAllByTimeAndQuick(DateFormatUtils.format(day, 'yyyy-MM-dd'), new Doctor(id : doctorId))
			} else {
					dateBookingTime = bookingTimeRepository.findByDoctorAndTime(new Doctor(id : doctorId), day) // default 3
					
					selfBookingTimes = bookingSelfTimeRepository.findByDoctorAndTime(new Doctor(id : doctorId), day)
					
					bookingList = bookingRepository.findAllByTime(DateFormatUtils.format(day, 'yyyy-MM-dd'), new Doctor(id : doctorId)) // 订单占用 2
			}

			dayData << [
				'dayId' : i + 1,
				'dayOfWeek' : idx,
				'day' : DateFormatUtils.format(day, 'yyyy-MM-dd')
			]
			
			if (dateBookingTime) {
				timeModel = dateBookingTime.bookingModel // default
			} 
			
			if (selfBookingTimes && selfBookingTimes.size() > 0) {
				selfBookingTimes.each {
					def start, end
					if (DateUtils.isSameDay(day, it.startTime)) {
						start = Integer.valueOf(DateFormatUtils.format(it.startTime, 'HH'))
						if (DateUtils.isSameDay(it.startTime, it.endTime)) {
							end = Integer.valueOf(DateFormatUtils.format(it.endTime, 'HH'))
						} else {
							end = 23
						}
					} else if (!DateUtils.isSameDay(day, it.startTime) && it.startTime.before(day)) {
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
				}
			}
			if (DateUtils.isSameDay(usedTime, day) && usedTime.compareTo(day) > -1) {
				def index = Integer.valueOf(DateFormatUtils.format(usedTime, 'HH'))
				for (j in 0..index){
					timeModel = timeModel.substring(0, j) + '1' + timeModel.substring(j + 1)
				}
			} else if (!DateUtils.isSameDay(usedTime, day) && usedTime.compareTo(day) == 1) {
				timeModel = "111111111111111111111111"
			}

			bookingList.each {
				if (it.outDated && (new Date()).getTime() > it.outDated.getTime()) {
					it.outDated = null
					it.time = null
					outDateBooking << it
				}
				if (it.time) {
					def index = Integer.valueOf(DateFormatUtils.format(it.time, 'HH')),
					occupy = timeModel.substring(index, index + 1)
					if ("1" != occupy || !"1".equals(occupy)) {
						timeModel = timeModel.substring(0, index) + '1' + timeModel.substring(index + 1)
					}
				} 
			}
			for (k in 0..timeModel.length() - 1) {
				if ('0' == timeModel[k] || '0'.equals(timeModel[k])) {
					timeData << [
						'dayId': i + 1,
						'startTime' : DateFormatUtils.format(DateUtils.parseDate("$k:00", 'HH:mm'), 'HH:mm'),
						'endTime' : DateFormatUtils.format(DateUtils.parseDate("${k+1}:00", 'HH:mm'), 'HH:mm')
					]
				}
			}
		}

		bookingRepository.save(outDateBooking)
		[
			'success' : '1',
			'dayData' : dayData,
			'timeData' : timeData
		]
	}
	
	/**
	 * @param month
	 * @param doctorId
	 * @param user
	 * @return
	 * 用户获取医师每月列表
	 */
	@RequestMapping(value = 'retrieveNextFreeTime', method = RequestMethod.GET)
	retrieveNextFreeTime(@RequestParam(required = false) String time, @RequestParam long doctorId, @ModelAttribute('currentUser') User user) {

		
		def date
		if (time == null) {
			date = new Date()
		} else {
			date = DateUtils.parseDate(time, 'yyyy-MM-dd')
		 }
		
		def doctorInfo = doctorInfoRepository.findOne(doctorId),
			bookingTime
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
//				timeList = bookingTimeRepository.findAllByTime(DateFormatUtils.format(day, 'yyyy-MM-dd'), new Doctor(id : doctorId)),
				bookingList = bookingRepository.findAllByTime(DateFormatUtils.format(day, 'yyyy-MM-dd'), new Doctor(id : doctorId))

			bookingList.each {
				def index = Integer.valueOf(DateFormatUtils.format(it.time, 'HH')),
					occupy = timeModel.substring(index, index + 1)
				if ("1" != occupy || !"1".equals(occupy)) {
					timeModel = timeModel.substring(0, index) + '1' + timeModel.substring(index + 1)
				}
			}
			for (k in 0..timeModel.length() - 1) {
				if ('0' == timeModel[k] || '0'.equals(timeModel[k])) {
					timeData << [
						'startTime' : DateFormatUtils.format(DateUtils.parseDate("$k:00", 'HH:mm'), 'HH:mm'),
						'endTime' : DateFormatUtils.format(DateUtils.parseDate("${k+1}:00", 'HH:mm'), 'HH:mm')
					]
				}
			}
			result << [
				'dayOfWeek' : idx,
				'day' : DateFormatUtils.format(c.getTime(), 'yyyy-MM-dd'),
				'timeData' : timeData
			]
		}
		[
			'success' : '1',
			'name' : doctorInfo?.name,
			'address' : doctorInfo?.address,
			'introduce' : doctorInfo?.introduce,
			'field' : doctorInfo?.field,
			'targetUser' : doctorInfo?.targetUser,
			'genre' : doctorInfo?.genre,
			'faceToFace' : doctorInfo?.faceToFace,
			'video' : doctorInfo?.video,
			'avatar' : doctorInfo?.avatarPath,
			'result' : result
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
	submitBookingTime(@RequestParam Long bookingId, @RequestParam String time,
		@RequestParam(required = false) Boolean quick,
		@RequestParam(required = false) Integer money,
		@ModelAttribute('currentUser') User user) {
		
		logger.trace '-- 提交预约时间 --'

		def booking = bookingRepository.findOne(bookingId)
		if (time) {
			def date = DateUtils.parseDate(time, 'yyyy-MM-dd HH')
			def today = DateUtils.parseDate(DateFormatUtils.format(new Date(), 'yyyy-MM-dd HH'), 'yyyy-MM-dd HH')
			if (date < today) {
				return '{"success" : "0", "message" : "err801"}'
			}
			booking.time = DateUtils.parseDate(time, 'yyyy-MM-dd HH')
		}
		if (quick != null)
			booking.quick = quick
		if (money != null)
			booking.money = money
		
		bookingRepository.save(booking)
		'{"success" : "1"}'
	}
	
	/**
	 * @param user
	 * @return
	 * 获取最后订单
	 */
	@RequestMapping(value = 'retrieveLastBooking', method = RequestMethod.GET)
	retrieveLastBooking(@ModelAttribute('currentUser') User user) {
		
		def booking = bookingRepository.findMaxBooking(user)
		
		if (booking) {
			[
				'success' : '1',
				'bookingId' : booking?.id,
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
				'haveConsulted' : booking?.haveConsulted,
				'userStatus' : booking?.userStatus,
				'doctorStatus' : booking?.doctorStatus
			]
		} else {
			'{"success" : "0", "message" : "没有订单"}'
		}
	}

	/**
	 * 获取订单列表
	 * @param page
	 * @param size
	 * @param user
	 * @return
	 */
	@RequestMapping(value = 'retrieveSelfBooking')
	retrieveSelfBooking(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size,
						@ModelAttribute('currentUser') User user) {

		if (page == null) {
			page = 0
		}
		if (size == null) {
			size = 20
		}

		// 设置用户订单信息 医师，诊所为false，以查看
		def userBookingDatas = userBookingDataRepository.findAll(
				[
						user_equal        : user,
						thirdRefresh_equal: true
				]
		)
		userBookingDatas.each {
			it.thirdRefresh = false
		}
		userBookingDataRepository.save(userBookingDatas)


		def uas = userAuthorizationRepository.findAll(
				[
						user_equal: user
				]
		)
		Pageable pageable = new PageRequest(page, size, Direction.DESC, 'createdDate')
		def bookings = bookingRepository.findAll(
				[
						user_equal     : user,
						status_notEqual: Booking.Status.Close
				], pageable
		)
		def bookingContent = bookings.content, data = []
		def outDateBooking = [], now = new Date()
		bookingContent.each {
			def outSecond
			if (it.status == Booking.Status.Paying && it.outDated <= now) {
				it.time = null
				it.outDated = null
				it.status = Booking.Status.Accept
				outDateBooking << it
			}
			if (it.outDated != null) {
				outSecond = (it.outDated.time - now.time) / 1000 as int
			}
			def doctorName
			if (it.doctor) {
				doctorName = it.doctor?.doctorInfo?.name
			} else {
				doctorName = it.doctorName
			}
			def authorise = uas.any { auth ->
				auth.doctor == it.doctor
			}

			data << [
					bookingId        : it.id,
					doctorId         : it.doctor?.id,
					doctorName       : doctorName,
					doctorPhone      : it.doctor?.doctorInfo?.phone,
					lastBookingTime  : it.lastBookingTime,
					hospitalId       : it.hospital?.id,
					hospitalName     : it.hospital?.hospitalInfo?.name,
					hospitalPhone    : it.hospital?.hospitalInfo?.phone,
					name             : it.name,
					phone            : it.phone,
					sex              : it.sex?.ordinal(),
					birthday         : it.birthday,
					profession       : it.profession,
					onlineFee        : it.doctor?.doctorInfo?.onlineFee,
					offlineFee       : it.doctor?.doctorInfo?.offlineFee,
					city             : it.city,
					married          : it.married,
					haveChildren     : it.haveChildren,
					helpReason       : it.helpReason,
					otherProblem     : it.otherProblem,
					treatmented      : it.treatmented,
					haveConsulted    : it.haveConsulted,
					refusalReason    : it.refusalReason,
					time             : it.time,
					quick            : it.quick,
					consultType      : it.consultType?.ordinal(),
					status           : it.status?.ordinal(),
					baseMoney        : it.money,
					money            : it.money + it.quickMoney,
					userStatus       : it.userStatus?.ordinal(),
					doctorStatus     : it.doctorStatus?.ordinal(),
					doctorAvatar     : it.doctor?.doctorInfo?.avatarPath,
					hospitalAvatar   : it.hospital?.hospitalInfo?.hospitalAvatar,
					outSecond        : outSecond,
					doctorAddress    : it.doctor?.doctorInfo?.address,
					hospitalAddress  : it.hospital?.hospitalInfo?.address,
					hospitalMinCharge: it.hospital?.hospitalInfo?.minCharge,
					hospitalMaxCharge: it.hospital?.hospitalInfo?.maxCharge,
					authorise        : authorise
			]
			bookingService.saveBooking(outDateBooking)
		}
		[
				'success': '1',
				'more'   : bookings.hasNext(),
				'data'   : data
		]
	}
	
	/**
	 * @param user
	 * @return
	 * 获取订单列表
	 */
//	@Transactional
	/*@RequestMapping(value = 'retrieveSelfBooking', method = RequestMethod.POST)
	retrieveSelfBooking(
			@PageableDefault(page = 0, size = 20, sort = 'createdDate', direction = Direction.DESC)
					Pageable pageable,
			@ModelAttribute('currentUser') User user) {

		def bookings = bookingRepository.findAll(
				[
						user_equal     : user,
						status_notEqual: Booking.Status.Close
				], pageable
		)
		def bookingContent = bookings.getContent()
		def data = []

		def outDateBooking = [], now = new Date()
		bookingContent.each {
			def outSecond
			if (it.status == Booking.Status.Paying && it.outDated <= now) {
				it.time = null
				it.outDated = null
				it.status = Booking.Status.Accept
//				bookingRepository.save(it)
				outDateBooking << it
			}
			if (it.outDated != null) {
				outSecond = (it.outDated.time - now.time) / 1000 as int
			}
			def doctorName
			if (it?.doctor) {
				doctorName = it?.doctor?.doctorInfo?.name
			} else {
				doctorName = it?.doctorName
			}

			data << [
					'bookingId'        : it?.id,
					'doctorId'         : it?.doctor?.id,
					'doctorName'       : doctorName,
					'doctorPhone'      : it?.doctor?.doctorInfo?.phone,
					'lastBookingTime'  : it?.lastBookingTime,
					'hospitalId'       : it?.hospital?.id,
					'hospitalName'     : it?.hospital?.hospitalInfo?.name,
					'hospitalPhone'    : it?.hospital?.hospitalInfo?.phone,
					'name'             : it?.name,
					'phone'            : it?.phone,
					'sex'              : it?.sex?.ordinal(),
					'birthday'         : it?.birthday,
					'profession'       : it?.profession,
					'onlineFee'        : it?.doctor?.doctorInfo?.onlineFee,
					'offlineFee'       : it?.doctor?.doctorInfo?.offlineFee,
					'city'             : it?.city,
					'married'          : it?.married,
					'haveChildren'     : it?.haveChildren,
					'helpReason'       : it?.helpReason,
					'otherProblem'     : it?.otherProblem,
					'treatmented'      : it?.treatmented,
					'haveConsulted'    : it?.haveConsulted,
					'refusalReason'    : it?.refusalReason,
					'time'             : it?.time,
					'quick'            : it?.quick,
					'consultType'      : it?.consultType?.ordinal(),
					'status'           : it?.status?.ordinal(),
					'baseMoney'        : it.money,
					'money'            : it?.money + it?.quickMoney,
					'userStatus'       : it?.userStatus?.ordinal(),
					'doctorStatus'     : it?.doctorStatus?.ordinal(),
					'doctorAvatar'     : it?.doctor?.doctorInfo?.avatarPath,
					'hospitalAvatar'   : it?.hospital?.hospitalInfo?.hospitalAvatar,
					'outSecond'        : outSecond,
					'doctorAddress'    : it?.doctor?.doctorInfo?.address,
					'hospitalAddress'  : it?.hospital?.hospitalInfo?.address,
					'hospitalMinCharge': it?.hospital?.hospitalInfo?.minCharge,
					'hospitalMaxCharge': it?.hospital?.hospitalInfo?.maxCharge
			]
		}
		bookingService.saveBooking(outDateBooking)

		[
				'success': '1',
				'more'   : bookings.hasNext(),
				'data'   : data
		]

	}*/
		
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

	
	/**
	 * @param bookingId
	 * @param idx
	 * @param doctor
	 * @return
	 * 修改用户订单状态
	 */
	@Transactional
	@RequestMapping(value = 'confirmUserBookingStatus', method = RequestMethod.POST)
	confirmUserBookingStatus(@RequestParam long bookingId,
		@RequestParam(required = false) Integer idx,
		@ModelAttribute('currentUser') User user) {
		
		logger.trace '--- 修改医师订单状态 ---'
		def booking = bookingRepository.findOne(bookingId)
		
		if (idx != null) {
			booking.userStatus = Booking.BookStatus.values()[idx]
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

		def userBookingData = userBookingDataRepository.findOneByFilters(
				[
						user_equal   : user,
						booking_equal: booking
				]
		)
		userBookingData.userRefresh = true

		userBookingDataRepository.save(userBookingData)
		
		bookingRepository.save(booking)
		'{"success" : "1"}'
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
		@ModelAttribute('currentUser') User user) {
		
		def booking = bookingRepository.findOne(id)

		if (booking.status != Booking.Status.Paid && booking.quick) {
			return '{"success" : "0", "message" : "err812"}'
		}
		
		if (date){
			booking.time = DateUtils.parseDate(date, 'yyyy-MM-dd HH')
			booking.status = Booking.Status.ChangeDate
		}
		bookingRepository.save(booking)
		'{"success" : "1"}'
	}
		
	/**
	 * @return
	 * 已约下次
	 */
	@RequestMapping(value = 'bookingNext', method = RequestMethod.POST)
	bookingNext(@RequestParam long bookingId,
		@ModelAttribute('currentUser') User user) {
		
		logger.trace '-- 已约下次 --'
		
		def booking = bookingRepository.findOne(bookingId)
		booking.status = Booking.Status.Next
		
		def nextBooking = new Booking(
			user : booking.user,
			name : booking.name,
			phone : booking.phone,
			sex : booking.sex,
			birthday : booking.birthday,
			profession : booking.profession,
			city : booking.city,
			married : booking.married,
			haveChildren : booking.haveChildren,
			helpReason : booking.helpReason,
			otherProblem : booking.otherProblem,
			treatmented : booking.treatmented,
			haveConsulted : booking.haveConsulted,
			refusalReason : booking.refusalReason,
//			time : new Date(),
			quick : false,
			consultType : booking.consultType,
			status : Booking.Status.Accept,
			money : booking.money,
			lastBookingTime : booking.time
		)
		if (booking.doctor != null) {
			nextBooking.doctor = booking.doctor
			def doctorId = booking.doctor.id
			nextBooking.tradeNo = "u${user.id}d${doctorId}s${System.currentTimeMillis()}"
		}
		if (booking.hospital != null) {
			nextBooking.hospital = booking.hospital
			def hospitalId = booking.hospital.id
			nextBooking.tradeNo = "u${user.id}h${hospitalId}s${System.currentTimeMillis()}"
		}
		nextBooking = bookingRepository.save(nextBooking)

		// 保存用户订单状态
		def userBookingData = new UserBookingData(
				user: user,
				booking: nextBooking
		)
		userBookingDataRepository.save(userBookingData)

		bookingRepository.save(booking)
		[
			'success' : '1',
			'bookingId' : nextBooking?.id,
			'doctorId' : nextBooking?.doctor?.id,
			'doctorName' : nextBooking?.doctor?.doctorInfo?.name,
			'doctorPhone' : nextBooking?.doctor?.doctorInfo?.phone,
			'lastBookingTime' : nextBooking?.lastBookingTime,
			'hospitalId' :	nextBooking?.hospital?.id,
			'hospitalName' : nextBooking?.hospital?.hospitalInfo?.name,
			'name' : nextBooking?.name,
			'phone' : nextBooking?.phone,
			'sex' : nextBooking?.sex?.ordinal(),
			'birthday' : nextBooking?.birthday,
			'profession' : nextBooking?.profession,
			'onlineFee' : nextBooking?.doctor?.doctorInfo?.onlineFee,
			'offlineFee' : nextBooking?.doctor?.doctorInfo?.offlineFee,
			'city' : nextBooking?.city,
			'married' : nextBooking?.married,
			'haveChildren' : nextBooking?.haveChildren,
			'helpReason' : nextBooking?.helpReason,
			'otherProblem' : nextBooking?.otherProblem,
			'treatmented' : nextBooking?.treatmented,
			'haveConsulted' : nextBooking?.haveConsulted,
			'refusalReason' : nextBooking?.refusalReason,
			'time' : nextBooking?.time,
			'quick' : nextBooking?.quick,
			'consultType' : nextBooking?.consultType?.ordinal(),
			'status' : nextBooking?.status?.ordinal(),
			'baseMoney' : nextBooking?.money,
			'userStatus' : nextBooking?.userStatus,
			'doctorStatus' : nextBooking?.doctorStatus,
			'doctorAvatar' : nextBooking?.doctor?.doctorInfo?.avatarPath
		]
	}

	/**
	 * 关闭订单
	 * @param id
	 * @param user
	 */
	@RequestMapping(value = 'removeBooking', method = RequestMethod.POST)
	removeBooking(@RequestParam long id,
				  @ModelAttribute('currentUser') User user) {

		if (null == user.getId()) {
			return '{"success" : "0", "message" : "err000"}'
		}

		def booking = bookingRepository.findOne(id)

		booking.status = Booking.Status.Close
		booking.time = null
		booking.outDated = null

		bookingRepository.save(booking)

		'{"success" : "1"}'
	}

}
