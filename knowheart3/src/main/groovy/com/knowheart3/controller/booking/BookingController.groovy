package com.knowheart3.controller.booking
import com.knowheart3.repository.booking.BookingRepository
import com.knowheart3.repository.booking.BookingSelfTimeRepository
import com.knowheart3.repository.booking.BookingTimeRepository
import com.knowheart3.repository.doctor.DoctorCashLogRepository
import com.knowheart3.repository.doctor.DoctorCashRepository
import com.knowheart3.repository.doctor.DoctorInfoRepository
import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.booking.Booking
import com.qubaopen.survey.entity.booking.ResolveType
import com.qubaopen.survey.entity.cash.DoctorCash
import com.qubaopen.survey.entity.cash.DoctorCashLog
import com.qubaopen.survey.entity.doctor.Doctor
import com.qubaopen.survey.entity.hospital.Hospital
import com.qubaopen.survey.entity.user.User
import org.apache.commons.lang3.time.DateFormatUtils
import org.apache.commons.lang3.time.DateUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort.Direction
import org.springframework.data.web.PageableDefault
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
	BookingTimeRepository bookingTimeRepository
	
	@Autowired
	BookingSelfTimeRepository bookingSelfTimeRepository

	@Autowired
	DoctorCashRepository doctorCashRepository

	@Autowired
	DoctorCashLogRepository doctorCashLogRepository

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
		@ModelAttribute('currentUser') User user
		) {
		
		if (!validatePhone(phone)) {
			return '{"success" : "0", "message": "err003"}'
		}
        if (!doctorId && !hospitalId) {
            return '{"success" : "0", "message" : "err810"}'
        }

		def tradeNo
		if (doctorId != null) {
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
			tradeNo = "u${user.id}h${hospitalId}t${DateFormatUtils.format(new Date(), 'yyyyMMddHHmmssssss')}"

            def exist = bookingRepository.findExistHospitalBooking(user.id, hospitalId)

            if (exist && exist.size() > 0) {
                return '{"success" : "0", "message" : "err809"}'
            }
		}

		def sex
		def booking = new Booking(
			tradeNo : tradeNo,
			user : user,
			name : name,
			phone : phone,
			profession : profession,
			city : city,
			married : married,
			haveChildren : haveChildren,
			helpReason : helpReason,
			otherProblem : otherProblem,
			treatmented : treatmented,
			haveConsulted : haveConsulted,
            resolveType: ResolveType.None
//			time : new Date()
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
		if (time)
			def date = DateUtils.parseDate(time, 'yyyy-MM-dd HH')
			def today = DateUtils.parseDate(DateFormatUtils.format(new Date(), 'yyyy-MM-dd HH'), 'yyyy-MM-dd HH')
			if (date < today) {
				return '{"success" : "0", "message" : "err801"}'
			}
			booking.time = DateUtils.parseDate(time, 'yyyy-MM-dd HH')
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
			'{"success" : "0", "message" : "err"}'
		}
	}
	
	/**
	 * @param user
	 * @return
	 * 获取订单列表
	 */
	@Transactional
	@RequestMapping(value = 'retrieveSelfBooking', method = RequestMethod.POST)
	retrieveSelfBooking(
//		@RequestParam(required = false) String ids,
		@PageableDefault(page = 0, size = 20, sort = 'createdDate', direction = Direction.DESC)
		Pageable pageable,
		@ModelAttribute('currentUser') User user) {
		
		def bookings = bookingRepository.findAll(
			[
				user_equal : user
			], pageable
		)
		def bookingContent = bookings.getContent()
		def data = [], more = true
		
		if (bookingContent.size() < pageable.pageSize) {
			more = false
		}
		def outDateBooking = []
		bookingContent.each {
		    def outSecond, now = new Date()
		    if (it.outDated > now) {
		        it.time = null
		        it.outDated = null
		        outDateBooking << it
		    }
		    if (it.outDated != null) {
		        outSecond = (now.time - it.outDated.time) / 1000 as int
		    }
			data << [
				'bookingId' : it?.id,
				'doctorId' : it?.doctor?.id,
				'doctorName' : it?.doctor?.doctorInfo?.name,
				'doctorPhone' : it?.doctor?.doctorInfo?.phone,
				'lastBookingTime' : it?.lastBookingTime,
				'hospitalId' :	it?.hospital?.id,
				'hospitalName' : it?.hospital?.hospitalInfo?.name,
				'hospitalPhone' : it?.hospital?.hospitalInfo?.phone,
				'name' : it?.name,
				'phone' : it?.phone,
				'sex' : it?.sex?.ordinal(),
				'birthday' : it?.birthday,
				'profession' : it?.profession,
				'onlineFee' : it?.doctor?.doctorInfo?.onlineFee,
				'offlineFee' : it?.doctor?.doctorInfo?.offlineFee,
				'city' : it?.city,
				'married' : it?.married,
				'haveChildren' : it?.haveChildren,
				'helpReason' : it?.helpReason,
				'otherProblem' : it?.otherProblem,
				'treatmented' : it?.treatmented,
				'haveConsulted' : it?.haveConsulted,
				'refusalReason' : it?.refusalReason,
				'time' : it?.time,
				'quick' : it?.quick,
				'consultType' : it?.consultType?.ordinal(),
				'status' : it?.status?.ordinal(),
				'money' : it?.money,
				'userStatus' : it?.userStatus,
				'doctorStatus' : it?.doctorStatus,
				'doctorAvatar' : it?.doctor?.doctorInfo?.avatarPath,
				'hospitalAvatar' : it?.hospital?.hospitalInfo?.hospitalAvatar,
				'outSecond' : outSecond
			]
		}
		bookingRepository.save(outDateBooking)

		[
			'success' : '1',
			'more' : more,
			'data' : data	
		]
		
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
			booking.status == Booking.Status.Consulted

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
					payStatus: DoctorCashLog.PayStatus.Completed
			)
			doctorCashLogRepository.save(doctorCashLog)
		}
        booking.resolveType = ResolveType.None
        booking.sendEmail = false
		
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
		
		if (booking?.quick) {
			booking.status = Booking.Status.Paid
		} else {
			booking.status = Booking.Status.PayAccept
		}
		
		if (date)
			booking.time = DateUtils.parseDate(date, 'yyyy-MM-dd HH')
		
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
			'money' : nextBooking?.money,
			'userStatus' : nextBooking?.userStatus,
			'doctorStatus' : nextBooking?.doctorStatus,
			'doctorAvatar' : nextBooking?.doctor?.doctorInfo?.avatarPath
		]
	}

}
