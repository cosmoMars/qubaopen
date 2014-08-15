package com.qubaopen.survey.controller.user

import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang3.time.DateFormatUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttributes
import org.springframework.web.multipart.MultipartFile

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.user.UserInfo
import com.qubaopen.survey.repository.user.UserInfoRepository
import com.qubaopen.survey.service.user.UserInfoService

@RestController
@RequestMapping('userInfos')
@SessionAttributes('currentUser')
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
	@RequestMapping(value = 'retrievePersonalInfo', method = RequestMethod.GET)
	retrievePersonalInfo(@ModelAttribute('currentUser') User user) {

		logger.trace ' -- 获得用户个人信息 -- '

		userInfoService.retrievePersonalInfo(user.id)
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
			println e.message
			'{"success": "0", "message": "err014"}'
		}

	}


	/**
	 * 上传头像
	 * @param userId
	 * @param avatar
	 */
	@RequestMapping(value = 'uploadAvatar', method = RequestMethod.POST, consumes = 'multipart/form-data')
	uploadAvatar(@RequestParam(required = false) MultipartFile avatar, @ModelAttribute('currentUser') User user, HttpServletRequest request) {

		logger.trace(' -- 上传头像 -- ')

		if (avatar) {

			def filename = "${user.id}_${DateFormatUtils.format(new Date(), 'yyyyMMdd-HH:mm:ss')}.png",
				avatarPath = "${request.getServletContext().getRealPath('/')}pic/$filename"

			println avatarPath

			saveFile(avatar.bytes, avatarPath)

			def userInfo = user.userInfo
			userInfo.avatarPath = "/know-heart/pic/$filename"
			userInfoRepository.save(userInfo)
			return '{"success": "1"}'
		}
		'{"success": "0"}'
	}

	private void saveFile(byte[] bytes, String filename) {
		def fos = new FileOutputStream(filename)
		fos.write(bytes)
		fos.close()
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
		@RequestParam(required = false) Boolean sharedSina,
		@RequestParam(required = false) Boolean sharedTencent,
		@RequestParam(required = false) Boolean sharedWeChatFriend,
		@RequestParam(required = false) Boolean sharedQQSpace,
		@RequestParam(required = false) Boolean sharedWeChat,
		@RequestParam(required = false) Boolean publicAnswersToChief,
		@RequestParam(required = false) Boolean publicMovementToFriend,
		@RequestParam(required = false) Boolean publicAnswersToFriend
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