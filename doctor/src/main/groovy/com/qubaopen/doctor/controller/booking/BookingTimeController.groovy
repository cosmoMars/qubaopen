package com.qubaopen.doctor.controller.booking;

import org.apache.commons.lang3.time.DateUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttributes

import com.fasterxml.jackson.databind.node.ArrayNode
import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.doctor.repository.booking.BookingSelfTimeRepository
import com.qubaopen.doctor.repository.booking.BookingTimeRepository
import com.qubaopen.survey.entity.booking.BookingSelfTime
import com.qubaopen.survey.entity.booking.BookingTime
import com.qubaopen.survey.entity.doctor.Doctor


@RestController
@RequestMapping('bookingTime')
@SessionAttributes('currentDoctor')
public class BookingTimeController extends AbstractBaseController<BookingTime, Long> {

	@Autowired
	BookingTimeRepository bookingTimeRepository
	
	@Autowired
	BookingSelfTimeRepository bookingSelfTimeRepository
	
	@Override
	MyRepository<BookingTime, Long> getRepository() {
		return bookingTimeRepository
	}
		
	/**
	 * @param json
	 * @param doctor
	 * @return
	 * 修改时间作息
	 */
	@RequestMapping(value = 'modifyBookingTimeByJson', method = RequestMethod.POST)
	modifyBookingTimeByJson(@RequestParam(required = false) String json, @ModelAttribute('currentDoctor') Doctor doctor) {
	
		if (json == null) {
			return '{"success" : "0", "message" : "err909" }' // 没有json
		}
		logger.debug(" =============== {}", json)
		
		def jsons = json.split("=")
		
		def jsonNodes = objectMapper.readTree(jsons[1]),
			bookingModels = []
		jsonNodes.each {
			
			def dateTime = it.get('date').asText()
			if (dateTime == null) {
				'{"success" : "0", "message" : "err907"}' // 时间不正确
			}
			def dbTime = bookingTimeRepository.findByFormatTime(doctor, dateTime)
			def strTime
			if (dbTime) {
				strTime = dbTime.bookingModel
			} else {
				strTime = '000000000000000000000000'
			}
			def hours = (ArrayNode)it.path('times')
			
			if (it.get('type') == null) {
				return '{"success" : "0", "message" : "err905"}'// 没有预约时间类型
			}
			def type = it.get('type').asInt()
				
			if (type == 0) { // default 修改有空
				hours.each { h ->
					def idx = h.asInt()
					strTime = strTime.substring(0, idx) + '0' + strTime.substring(idx + 1)
				}
			} else if (type == 1) { // default 修改没空
				hours.each { h ->
					def idx = h.asInt()
					strTime = strTime.substring(0, idx) + '1' + strTime.substring(idx + 1)
				}
			}
//			def location, content
//			if (it.get('location') != null) {
//				location = it.get('location').asText()
//			}
//			if (it.get('content') != null) {
//				content = it.get('content').asText()
//			}
			if (dbTime) {
				dbTime.bookingModel = strTime
			} else {
				dbTime = new BookingTime(
					doctor : doctor,
					time : DateUtils.parseDate(dateTime, 'yyyy-MM-dd'),
					bookingModel : strTime
				)
			}
			bookingModels << dbTime
		}
		
		bookingTimeRepository.save(bookingModels)
		'{"success" : "1"}'
		
	}
	

	/**
	 * @param id
	 * @param doctor
	 * @return
	 * 删除行程
	 */
	@RequestMapping(value = 'deleteSelfTime', method = RequestMethod.POST)
	deleteSelfTime(@RequestParam Long id, @ModelAttribute('currentDoctor') Doctor doctor) {
		
		if (id == null) {
			return '{"success" : "0", "message" : "err906"}'
		}
		def selfTime = bookingSelfTimeRepository.findOne(id)
		
		if (!selfTime) {
			return '{"success" : "0", "message" : "err906"}'
		}
		
		bookingSelfTimeRepository.delete(selfTime)
		'{"success" : "1"}'
	}
	
	/**
	 * @param strDate
	 * @param content
	 * @param startTime
	 * @param endTime
	 * @param location
	 * @param doctor
	 * @return
	 * 添加自我时间
	 */
	@RequestMapping(value = 'addSelfTime', method = RequestMethod.POST)
	addSelfTime(//@RequestParam(required = false) String strDate,
		@RequestParam(required = false) String content,
		@RequestParam(required = false) String startTime,
		@RequestParam(required = false) String endTime,
		@RequestParam(required = false) String location,
		@ModelAttribute('currentDoctor') Doctor doctor) {
		
//		def date = DateUtils.parseDate(strDate, 'yyyy-MM-dd')
		
		def start = DateUtils.parseDate(startTime, 'yyyy-MM-dd HH:mm'),
			end
		if (!endTime) {
			def c = Calendar.getInstance()
			c.setTime start
			c.add(Calendar.HOUR, 1)
			end = c.getTime()
		} else {
			end = DateUtils.parseDate(endTime, 'yyyy-MM-dd HH:mm')
		}
		
		def selfTime = new BookingSelfTime(
			doctor : doctor,
//			date : date,
			startTime : start,
			endTime : end
		)
		if (selfTime.endTime.before(selfTime.startTime)) {
			return '{"success" : "0", "message" : "err906"}' // 开始时间必须小于结束
		}
		if (content) {
			selfTime.content = content
		}
		if (location) {
			selfTime.location = location
		}
		
		bookingSelfTimeRepository.save(selfTime)
		[
			"success" : "1",
			"selfTimeId" : selfTime?.id	
		]
	}
	/**
	 * @param strDate
	 * @param content
	 * @param startTime
	 * @param endTime
	 * @param location
	 * @param doctor
	 * @return
	 * 修改自我时间
	 */
	@RequestMapping(value = 'modifySelfTime', method = RequestMethod.POST)
	modifySelfTime(@RequestParam(required = false) Long id,
//		@RequestParam(required = false) String strDate,
		@RequestParam(required = false) String content,
		@RequestParam(required = false) String startTime,
		@RequestParam(required = false) String endTime,
		@RequestParam(required = false) String location,
		@ModelAttribute('currentDoctor') Doctor doctor) {
		
		def selfTime =  bookingSelfTimeRepository.findOne(id)
		
//		if (strDate) {
//			def date = DateUtils.parseDate(strDate, 'yyyy-MM-dd')
//			selfTime.date = date
//		}
		if (startTime) {
			selfTime.startTime = DateUtils.parseDate(startTime, 'yyyy-MM-dd HH:mm')
		}
		if (endTime) {
			selfTime.endTime = DateUtils.parseDate(endTime, 'yyyy-MM-dd HH:mm')
		}
		if (selfTime.startTime && !selfTime.endTime) {
			def c = Calendar.getInstance()
			c.setTime selfTime.startTime
			c.add(Calendar.HOUR, 1)
			selfTime.endTime = c.getTime()
		}
		if (selfTime.endTime.before(selfTime.startTime)) {
			return '{"success" : "0", "message" : "err906"}' //开始时间必须小于结束
		}
		
		if (content) {
			selfTime.content = content
		}
		if (location) {
			selfTime.location = location
		}
		
		bookingSelfTimeRepository.save(selfTime)
		'{"success" : "1"}'
	}
		
	/**
	 * @param id
	 * @param doctor
	 * @return
	 * 获取相信医师自定义事件
	 */
	@RequestMapping(value = 'retrieveSelfTime/{id}')
	retrieveSelfTime(@PathVariable long id, @ModelAttribute('currentDoctor') Doctor doctor) {
		
		def selfTime = bookingSelfTimeRepository.findOne(id)
		[
			'success' : '1',
			'selfTimeId' : selfTime?.id,
			'startTime' : selfTime?.startTime,
			'endTime' : selfTime?.endTime,
			'location' : selfTime?.location,
			'content' : selfTime?.content
		]
	} 
}
