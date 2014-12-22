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
import com.qubaopen.doctor.repository.booking.BookingTimeRepository
import com.qubaopen.survey.entity.booking.BookingTime
import com.qubaopen.survey.entity.doctor.Doctor


@RestController
@RequestMapping('bookingTime')
@SessionAttributes('currentDoctor')
public class BookingTimeController extends AbstractBaseController<BookingTime, Long> {

	@Autowired
	BookingTimeRepository bookingTimeRepository
	
	@Override
	MyRepository<BookingTime, Long> getRepository() {
		return bookingTimeRepository
	}

	/**
	 * @param id
	 * @param doctor
	 * @return
	 * 查看详情
	 */
	@RequestMapping(value = 'retrieveBookingTime/{id}', method = RequestMethod.GET)
	retrieveBookingTime(@PathVariable long id, @ModelAttribute('currentDoctor') Doctor doctor) {
		
		def bookingTime = bookingTimeRepository.findOne(id)
		
		[
			'success' : '1',
			'bookingTimeId' : bookingTime?.id,
			'startTime' : bookingTime?.startTime,
			'endTime' : bookingTime?.endTime,
			'location' : bookingTime?.location,
			'content' : bookingTime?.content,
			'remindTime' : bookingTime?.remindTime,
			'repeatModel' : bookingTime?.repeatModel
		]
		
	}
	
	/**
	 * @param content
	 * @param startTime
	 * @param endTime
	 * @param location
	 * @param remindTime
	 * @param repeatModel
	 * @param doctor
	 * @return
	 * 添加行程
	 */
	@RequestMapping(value = 'addBookingTime', method = RequestMethod.POST)
	addBookingTime(@RequestParam(required = false) String content,
		@RequestParam(required = false) String startTime,
		@RequestParam(required = false) String endTime,
		@RequestParam(required = false) String location,
		@RequestParam(required = false) Integer remindTime,
		@RequestParam(required = false) String repeatModel,
		@ModelAttribute('currentDoctor') Doctor doctor) {
		
		def start = DateUtils.parseDate(startTime, 'yyyy-MM-dd HH'),
			end
		if (!endTime) {
			def c = Calendar.getInstance()
			c.setTime start
			c.add(Calendar.HOUR, 1)
			end = c.getTime()
		} else {
			end = DateUtils.parseDate(endTime, 'yyyy-MM-dd HH')
		}
		
		def bookingTime = new BookingTime(
			doctor : doctor,
			startTime : start,
			endTime : end
		)
		if (content) {
			bookingTime.content = content
		}
		if (location) {
			bookingTime.location = location
		}
		if (remindTime != null) {
			bookingTime.remindTime = remindTime
		}
		if (repeatModel) {
			bookingTime.repeatModel = repeatModel
		} else {
			bookingTime.repeatModel = '0000000'
		}
		
		bookingTimeRepository.save(bookingTime)
		'{"success" : "1"}'
	}
		
	@RequestMapping(value = 'addBookingTimeByJson', method = RequestMethod.POST)
	addBookingTimeByJson(@RequestParam(required = false) String json, @ModelAttribute('currentDoctor') Doctor doctor) {
	
		logger.debug(" =============== json = {}", json)
		
		def jsonNodes = objectMapper.readTree(json),
			bookingModels = []
		jsonNodes.each {
			def strTime = '000000000000000000000000',
				hours = (ArrayNode)it.path('times')
			hours.each { h ->
				def idx = h.asInt()
				strTime = strTime.substring(0, idx) + '1' + strTime.substring(idx + 1)
			}
			def location, content
			if (it.get('location')) {
				location = it.get('location').asText()
			}
			if (it.get('content')) {
				content = it.get('content').asText()
			}
			if (!it.get('type')) {
				return '{"success" : "0", "message" : "err905"}'// 没有预约时间类型
			}
			def bookingTime = new BookingTime(
				doctor : doctor,
				location : location,
				content : content,
				time : DateUtils.parseDate(it.get('date').asText(), 'yyyy-MM-dd'),
				bookingModel : strTime,
				type : BookingTime.Type.values()[it.get('type').asInt()]
			)
			bookingModels << bookingTime
		}
		
		bookingTimeRepository.save(bookingModels)
		'{"success" : "1"}'
		
	}
	
	/**
	 * @param timeId
	 * @param content
	 * @param startTime
	 * @param endTime
	 * @param location
	 * @param remindTime
	 * @param doctor
	 * @return
	 * 修改行程
	 */
	@RequestMapping(value = 'modifyBookingTime', method = RequestMethod.POST)
	modifyBookingTime(@RequestParam Long timeId,
		@RequestParam(required = false) String content,
		@RequestParam(required = false) String startTime,
		@RequestParam(required = false) String endTime,
		@RequestParam(required = false) String location,
		@RequestParam(required = false) String remindTime,
		@RequestParam(required = false) String repeatModel,
		@ModelAttribute('currentDoctor') Doctor doctor) {
		
		def bookingTime = bookingTimeRepository.findOne(timeId)
		
		if (content) {
			bookingTime.content = content
		}
		if (startTime) {
			bookingTime.startTime = DateUtils.parseDate(startTime, 'yyyy-MM-dd HH')
		}
		if (endTime) {
			bookingTime.endTime = DateUtils.parseDate(endTime, 'yyyy-MM-dd HH')
		}
		if (location) {
			bookingTime.location = location
		}
		if (remindTime) {
			bookingTime.remindTime = remindTime
		}
		if (repeatModel) {
			bookingTime.repeatModel = repeatModel
		}
		bookingTimeRepository.save(bookingTime)
		'{"success" : "1"}'
	}

	/**
	 * @param id
	 * @param doctor
	 * @return
	 * 删除行程
	 */
	@RequestMapping(value = 'deleteBookingTime/{id}', method = RequestMethod.POST)
	deleteBookingTime(@PathVariable long id, @ModelAttribute('currentDoctor') Doctor doctor) {
		
		def bookingTime = bookingTimeRepository.findOne(id)
		bookingTimeRepository.delete(bookingTime)
		'{"success" : "1"}'
	}

}
