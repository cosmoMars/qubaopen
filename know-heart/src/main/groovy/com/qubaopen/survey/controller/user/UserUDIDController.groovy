package com.qubaopen.survey.controller.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttributes

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.user.UserUDID
import com.qubaopen.survey.repository.user.UserUDIDRepository
import com.qubaopen.survey.service.user.UserUDIDService
import com.qubaopen.survey.utils.DateCommons

@RestController
@RequestMapping('userUDIDs')
@SessionAttributes('currentUser')
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
	@RequestMapping(value = 'retrieveUserUDID', method = RequestMethod.GET)
	retrieveUserUDID(@ModelAttribute('currentUser') User user) {

		logger.trace(" -- 获取用户UDID信息 -- ")

		userUDIDService.retrieveUserUDID(user.id)
	}

	/**
	 * 修改用户UDID信息
	 * @param udid
	 * @return
	 */
	@Override
	@RequestMapping(method = RequestMethod.PUT)
	modify(@RequestBody UserUDID udid) {

		userUDIDRepository.modify(udid)

		'{"success": "1"}'
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
	modifyUDID(@RequestParam(required = false) Boolean isPush,
		@RequestParam(required = false) String startTime,
		@RequestParam(required = false) String endTime,
		@ModelAttribute('currentUser') User user
		) {

		def userUDID = userUDIDRepository.findOne(user.id)
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
		'{"success": "1"}'
	}

}
