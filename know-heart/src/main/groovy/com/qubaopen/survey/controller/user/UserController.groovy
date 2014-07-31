package com.qubaopen.survey.controller.user

import static com.qubaopen.survey.utils.ValidateUtil.*

import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.lang3.RandomStringUtils
import org.apache.commons.lang3.time.DateUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.user.UserCaptcha
import com.qubaopen.survey.repository.user.UserCaptchaRepository
import com.qubaopen.survey.repository.user.UserIDCardBindRepository
import com.qubaopen.survey.repository.user.UserInfoRepository
import com.qubaopen.survey.repository.user.UserReceiveAddressRepository
import com.qubaopen.survey.repository.user.UserRepository
import com.qubaopen.survey.service.SmsService
import com.qubaopen.survey.service.user.UserService
import com.qubaopen.survey.utils.DateCommons

/**
 * @author mars 用户表
 */
@RestController
@RequestMapping('users')
class UserController extends AbstractBaseController<User, Long> {

	@Autowired
	UserRepository userRepository

	@Autowired
	UserCaptchaRepository userCaptchaRepository

	@Autowired
	UserInfoRepository userInfoRepository

	@Autowired
	UserReceiveAddressRepository userReceiveAddressRepository

	@Autowired
	UserIDCardBindRepository userIDCardBindRepository

	@Autowired
	SmsService smsService

	@Autowired
	UserService userService

	@Override
	protected MyRepository<User, Long> getRepository() {
		userRepository
	}

	/**
	 * 用户登录
	 * @param user
	 * @return
	 */
	@RequestMapping(value='login', method = RequestMethod.PUT)
	login(@RequestBody User user) {

		logger.trace ' -- 用户登录 -- '

		def loginUser = userRepository.login(user.phone,  DigestUtils.md5Hex(user.password))

		if (loginUser) {

			def userInfo = userInfoRepository.findOne(loginUser.id),
				userReceiveAddress = userReceiveAddressRepository.findByUserAndDefaultAddress(loginUser,true),
				userIdCardBind = userIDCardBindRepository.findByUser(loginUser)

			def result = [
				'success' : 1,
				'message' : '登录成功',
				'userId' : loginUser.id,
				'phone' : loginUser.phone ?: '',
				'name' : userInfo.name ?: '',
				'sex' : userInfo.sex ?: '',
				'nickName' : userInfo.nickName ?: '',
				'bloodType' : userInfo.bloodType.toString() ?: '',
				'district' : '',
				'email' : loginUser.email ?: '',
				'address' : userReceiveAddress?.detialAddress ?: '',
				'defaultAddressId' : userReceiveAddress?.id ?: '',
				'consignee' : userReceiveAddress?.consignee ?: '',
				'defaultAddressPhone' : userReceiveAddress?.phone ?: ''
			]
			if (userIdCardBind && userIdCardBind.userIDCard) {
				result << ['idCard' : userIdCardBind.userIDCard.IDCard ?: '']
			} else {
				result << ['idCard' : '']
			}
			if (userInfo?.birthday) {
				result << ['birthday' : DateCommons.Date2String(userInfo.birthday,'yyyy-MM-dd')]
			} else {
				result << ['birthday' : '']
			}
			return result
		}

		'{"success" : 0, "error": "err001"}'
	}

	/**
	 * 添加用户注册记录
	 * @param user
	 * @return
	 */
	@RequestMapping(value ='register', method = RequestMethod.POST, consumes = 'multipart/form-data')
	register(@RequestParam phone, @RequestParam password, @RequestParam captcha, @RequestParam MultipartFile avatar) {

		logger.trace ' -- 添加用户注册记录 -- '

		if (!validatePhone(phone)) {
			return '{"success" : 0, "error": "err003"}'
		}

		if (!validatePwd(password)) {
			return '{"success": 0, "error": "err004"}'
		}

		def u = userRepository.findByPhone(phone)
		if (u) {
			if (u.activated) {
				return '{"success": 0, "errror": "err005"}'
			}

			def userCaptcha = userCaptchaRepository.findOne(u.id)
			if (userCaptcha?.captcha != captcha) {
				return '{"success": 0, "errror": "err006"}'
			}

			u.password = DigestUtils.md5Hex(password)
			u.activated = true
			userService.saveUserAndUserAvatar(u, avatar)

			return '{"success": 1}'
		}

		'{"success": 0, "errror": "err007"}'
	}

	/**
	 * @author blues
	 * @param phone 用户手机号
	 * @return 判断手机用户，不存在创建，存在给用户手机发一条验证码短信
	 */
	@RequestMapping(value = 'sendCaptcha', method = RequestMethod.GET)
	sendCaptcha(@RequestParam String phone) {

		logger.trace ' -- 发送验证码 -- '
		logger.trace "phone := $phone"

		if (!validatePhone(phone)) { // 验证用户手机号是否无效
			return '{"success" : 0, "errror": "err003"}'
		}

		// 判断用户是否存在
		def user = userRepository.findByPhone(phone)
		if (!user) {
			user = new User(
				phone : phone
			)
			userRepository.save(user)
		}

		def userCaptcha = userCaptchaRepository.findOne(user.id)
		def today = new Date()
		if (userCaptcha) {
			def lastSentDate = userCaptcha.lastSentDate
			if ((today.time - lastSentDate.time) < 60000) {
				return '{"success": 0, "error": "err008"}'
			}

			if (userCaptcha.sentNum > 10) {
				return '{"success": 0, "error": "err009"}'
			}
		}

		// 生成6位数字格式的验证码
		def captcha = RandomStringUtils.randomNumeric(6)
		// 给指定的用户手机号发送6位随机数的验证码
		def success = smsService.sendCaptcha(phone, captcha)
		if (!success) {
			return '{"success": 0, "message": "err010"}'
		}

		if (userCaptcha) {
			userCaptcha.captcha = captcha
			userCaptcha.lastSentDate = today
			if (DateUtils.isSameDay(today, userCaptcha.lastSentDate)) {
				userCaptcha.sentNum++
			} else {
				userCaptcha.sentNum = 0
			}
		} else {
			userCaptcha = new UserCaptcha(
				id : user.id,
				captcha : captcha,
				lastSentDate : today,
				sentNum : 1
			)
		}

		userCaptchaRepository.save(userCaptcha)
		'{"success": 1}'
	}

	/**
	 * 忘记密码重置
	 * @param phone
	 * @param password
	 * @param captcha
	 * @return
	 */
	@RequestMapping(value = 'resetPassword', method = RequestMethod.POST)
	resetPassword(@RequestParam long userId, @RequestParam String password, @RequestParam String captcha) {

		logger.trace(" -- 忘记密码重置 -- ")

		if (StringUtils.isEmpty(captcha)) {
			return '{"success": 0, "message": "err011"}'
		}

		if (!validatePwd(password)) {
			return '{"success": 0, "message": "err004"}'
		}

		def user = userRepository.findOne(userId)
		if (!user) {
			return '{"success": 0, "message": "err001"}'
		}

		def userCaptcha = userCaptchaRepository.findOne(userId)
		if (!userCaptcha) {
			return '{"success": 0, "message": "err012"}'
		}

		if (captcha == userCaptcha.captcha) {
			user.password = DigestUtils.md5Hex(password)
			userRepository.save(user)
			return '{"success": 1}'
		}

		'{"success": 0, "message": "err006"}'
	}

}