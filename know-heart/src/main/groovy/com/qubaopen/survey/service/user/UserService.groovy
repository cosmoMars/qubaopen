package com.qubaopen.survey.service.user

import static com.qubaopen.survey.utils.ValidateUtil.*

import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.lang3.RandomStringUtils
import org.apache.commons.lang3.time.DateUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile

import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.user.UserCaptcha
import com.qubaopen.survey.entity.user.UserInfo
import com.qubaopen.survey.entity.user.UserUDID
import com.qubaopen.survey.repository.user.UserCaptchaRepository
import com.qubaopen.survey.repository.user.UserIDCardBindRepository
import com.qubaopen.survey.repository.user.UserInfoRepository
import com.qubaopen.survey.repository.user.UserReceiveAddressRepository
import com.qubaopen.survey.repository.user.UserRepository
import com.qubaopen.survey.repository.user.UserUDIDRepository
import com.qubaopen.survey.service.SmsService
import com.qubaopen.survey.utils.DateCommons

@Service
public class UserService {

	@Autowired
	UserRepository userRepository

	@Autowired
	UserInfoRepository userInfoRepository

	@Autowired
	UserUDIDRepository userUDIDRepository

	@Autowired
	UserCaptchaRepository userCaptchaRepository

	@Autowired
	UserReceiveAddressRepository userReceiveAddressRepository

	@Autowired
	UserIDCardBindRepository userIDCardBindRepository

	@Autowired
	SmsService smsService

	/** 用户登录
	 * @param user
	 * @return
	 */
	@Transactional
	login(User user) {

		def loginUser = userRepository.login(user.phone,  DigestUtils.md5Hex(user.password))

		if (loginUser) {

			def userInfo = userInfoRepository.findOne(loginUser.id),
				userReceiveAddress = userReceiveAddressRepository.findByUserAndTrueAddress(loginUser, true),
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
				'defaultAddress' : userReceiveAddress?.detialAddress ?: '',
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
	 * 用户注册
	 * @param phone
	 * @param password
	 * @param captcha
	 * @param avatar
	 * @return
	 */
	@Transactional
	register(String phone, String password, String captcha, MultipartFile avatar) {

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
			saveUserAndUserAvatar(u, avatar)


			return ['userId' : u.id]
		}

		'{"success": 0, "errror": "err007"}'
	}

	/**
	 * 发送验证码
	 * @param phone
	 * @return
	 */
	@Transactional
	sendCaptcha(String phone) {
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
				userCaptcha.sentNum ++
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
	 * 重置密码
	 * @param userId
	 * @param password
	 * @param captcha
	 * @return
	 */
	@Transactional
	resetPassword(long userId, String password, String captcha) {

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

	/**
	 * 保存user，新建userInfo，保存头像
	 * @param user
	 * @param avatar
	 * @return
	 */
	@Transactional
	void saveUserAndUserAvatar(User user, MultipartFile avatar) {

		user = userRepository.save(user)

		def userInfo = new UserInfo(
			id : user.id,
			publicAnswersToFriend : true,
			avatar : avatar.bytes
		)
		def userUdid = new UserUDID(
			id : user.id,
			push : true,
			startTime : DateCommons.String2Date('09:00','HH:mm'),
			endTime : DateCommons.String2Date('22:00','HH:mm')
		)

		userInfoRepository.save(userInfo)
		userUDIDRepository.save(userUdid)

	}

}
