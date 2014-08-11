package com.qubaopen.survey.controller.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.user.UserInfo
import com.qubaopen.survey.repository.user.UserInfoRepository
import com.qubaopen.survey.service.user.UserInfoService

@RestController
@RequestMapping('userInfos')
public class UserInfoController extends AbstractBaseController<UserInfo, Long> {

	@Autowired
	UserInfoRepository userInfoRepository

	@Autowired
	UserInfoService userInfoService

	@Override
	protected MyRepository<UserInfo, Long> getRepository() {
		userInfoRepository
	}

	/**
	 * 获取个人信息
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = 'retrievePersonalInfo/{userId}', method = RequestMethod.GET)
	retrievePersonalInfo(@PathVariable long userId) {

		logger.trace ' -- 获得用户个人信息 -- '

		userInfoService.retrievePersonalInfo(userId)
	}

	/**
	 * 修改个人信息
	 * @param userId
	 * @return
	 */
	@Override
	@RequestMapping(method = RequestMethod.PUT)
	modify(@RequestBody UserInfo userInfo) {
		try {
			userInfoRepository.modify(userInfo)

			'{"success": "1"}'
		} catch (Exception e) {
			e.printStackTrace()
			'{"success": "0", "message": "err014"}'
		}

	}


	/**
	 * 上传头像
	 * @param userId
	 * @param avatar
	 */
	@RequestMapping(value = 'uploadAvatar', method = RequestMethod.POST, consumes = 'multipart/form-data')
	uploadAvatar(@RequestParam long userId, @RequestParam(required = false) MultipartFile avatar) {

		logger.trace(' -- 上传头像 -- ')

		def userInfo = userInfoRepository.findOne(userId)
		userInfo.avatar = avatar.bytes
		try {
			userInfoRepository.save(userInfo)
			'{"success": "1"}'
		} catch (Exception e) {
			'{"success": "0", "message": "err102"}'
		}

	}

	/**
	 * 显示头像
	 * @param userId
	 * @param output
	 * @return
	 */
	@RequestMapping(value = 'retrieveAvatar/{userId}', method = RequestMethod.GET)
	retrieveAvatar(@PathVariable long userId, OutputStream output) {

		logger.trace(' -- 显示头像 -- ')

		def userInfo = userInfoRepository.findOne(userId)
		output.write(userInfo.avatar)
	}


	/**
	 * 修改用户信息 发布
	 * @param userId
	 * @param sharedSina
	 * @param sharedTencent
	 * @param sharedWeChatFriend
	 * @param sharedQQSpace
	 * @param sharedWeChat
	 * @param publicAnswersToChief
	 * @param publicMovementToFriend
	 * @param publicAnswersToFriend
	 * @return
	 */
	@RequestMapping(value = 'sharedUserInfo', method = RequestMethod.PUT)
	sharedUserInfo(@RequestParam long userId,
		@RequestParam(required = false) boolean sharedSina,
		@RequestParam(required = false) boolean sharedTencent,
		@RequestParam(required = false) boolean sharedWeChatFriend,
		@RequestParam(required = false) boolean sharedQQSpace,
		@RequestParam(required = false) boolean sharedWeChat,
		@RequestParam(required = false) boolean publicAnswersToChief,
		@RequestParam(required = false) boolean publicMovementToFriend,
		@RequestParam(required = false) boolean publicAnswersToFriend
		) {
			def userInfo = userInfoRepository.findOne(userId)
			if (sharedSina) {
				userInfo.sharedSina = sharedSina
			}
			if (sharedTencent) {
				userInfo.sharedSina = sharedTencent
			}
			if (sharedWeChatFriend) {
				userInfo.sharedSina = sharedWeChatFriend
			}
			if (sharedQQSpace) {
				userInfo.sharedSina = sharedQQSpace
			}
			if (sharedWeChat) {
				userInfo.sharedSina = sharedWeChat
			}
			if (publicAnswersToChief) {
				userInfo.sharedSina = publicAnswersToChief
			}
			if (publicMovementToFriend) {
				userInfo.sharedSina = publicMovementToFriend
			}
			if (publicAnswersToFriend) {
				userInfo.sharedSina = publicAnswersToFriend
			}
			userInfoRepository.save(userInfo)
			'{"success" : "1"}'
		}

}