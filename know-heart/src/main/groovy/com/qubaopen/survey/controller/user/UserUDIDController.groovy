package com.qubaopen.survey.controller.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.user.UserUDID
import com.qubaopen.survey.repository.user.UserUDIDRepository
import com.qubaopen.survey.service.user.UserUDIDService
import com.qubaopen.survey.utils.DateCommons

@RestController
@RequestMapping('userUDIDs')
public class UserUDIDController extends AbstractBaseController<UserUDID, Long> {

	@Autowired
	UserUDIDRepository userUDIDRepository

	@Autowired
	UserUDIDService userUDIDService

	@Override
	protected MyRepository<UserUDID, Long> getRepository() {
		userUDIDRepository
	}


	/**
	 * 获取用户UDID信息
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = 'retrieveUserUDID/{userId}', method = RequestMethod.GET)
	retrieveUserUDID(@PathVariable long userId) {

		logger.trace(" -- 获取用户UDID信息 -- ")

		userUDIDService.retrieveUserUDID(userId)
	}

	/**
	 * 修改用户UDID信息
	 * @param udid
	 * @return
	 */
	@Override
	@RequestMapping(method = RequestMethod.PUT)
	modify(@RequestBody @DateTimeFormat(pattern = 'HH:mm') UserUDID udid) {

		userUDIDRepository.modify(udid)

		'{"success": 1}'
	}

	/**
	 * 修改推送信息
	 * @param udidId
	 * @param isPush
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@RequestMapping(value = 'modifyUDID', method = RequestMethod.PUT)
	modifyUDID(@RequestParam(required = false) long udidId,
		@RequestParam(required = false) boolean isPush,
		@RequestParam(required = false) String startTime,
		@RequestParam(required = false) String endTime) {

		def userUDID = userUDIDRepository.findOne(udidId)
		if (isPush) {
			userUDID.push = isPush
		}
		if (startTime) {
			userUDID.startTime = DateCommons.String2Date(startTime, 'HH:mm')
		}
		if (endTime) {
			userUDID.endTime = DateCommons.String2Date(endTime, 'HH:mm')
		}
		userUDIDRepository.save(userUDID)
		'{"success": 1}'
	}

}
