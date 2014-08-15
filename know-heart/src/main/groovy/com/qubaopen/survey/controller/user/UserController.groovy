package com.qubaopen.survey.controller.user

import static com.qubaopen.survey.utils.ValidateUtil.*

import javax.servlet.http.HttpSession

import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.ui.Model
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
import com.qubaopen.survey.repository.user.UserReceiveAddressRepository
import com.qubaopen.survey.repository.user.UserRepository
import com.qubaopen.survey.service.user.UserService

/**
 * @author mars 用户表
 */
@RestController
@RequestMapping('users')
@SessionAttributes('currentUser')
class UserController extends AbstractBaseController<User, Long> {

	@Autowired
	UserRepository userRepository

	@Autowired
	UserService userService

	@Autowired
	UserReceiveAddressRepository userReceiveAddressRepository

	@Override
	protected MyRepository<User, Long> getRepository() {
		userRepository
	}

	/**
	 * 用户登录
	 * @param user
	 * @return
	 */
	@RequestMapping(value = 'login', method = RequestMethod.PUT)
	login(@RequestBody User user, Model model, HttpSession session) {

		logger.trace ' -- 用户登录 -- '

		if (!user) {
			return '{"success" : "0", "message": "err014"}'
		}

		if (!validatePhone(user.phone)) {
			return '{"success" : "0", "message": "err003"}'
		}

		if (!validatePwd(user.password)) {
			return '{"success": "0", "message": "err004"}'
		}

		def loginUser = userRepository.login(user.phone,  DigestUtils.md5Hex(user.password))

		if (loginUser) {

			model.addAttribute('currentUser', loginUser)

			def userInfo = loginUser.userInfo,
				userIdCardBind = loginUser.userIdCardBind,
				userReceiveAddress = userReceiveAddressRepository.findByUserAndTrueAddress(loginUser, true)

			return  [
				'success' : "1",
				'message' : '登录成功',
				'userId' : loginUser?.id,
				'phone' : loginUser?.phone,
				'name' : userInfo?.name,
				'sex' : userInfo?.sex,
				'nickName' : userInfo?.nickName,
				'bloodType' : userInfo?.bloodType,
				'district' : '',
				'email' : loginUser?.email,
				'defaultAddress' : userReceiveAddress?.detialAddress,
				'defaultAddressId' : userReceiveAddress?.id,
				'consignee' : userReceiveAddress?.consignee,
				'defaultAddressPhone' : userReceiveAddress?.phone,
				'idCard' : userIdCardBind?.userIDCard?.IDCard,
				'birthday' : userInfo?.birthday,
				'avatarPath' : userInfo?.avatarPath
			]
		}

		'{"success" : "0", "message": "err001"}'

	}

	/**
	 * 添加用户注册记录
	 * @param user
	 * @return
	 */
	@RequestMapping(value ='register', method = RequestMethod.POST, consumes = 'multipart/form-data')
	register(@RequestParam(required = false) String phone,
		@RequestParam(required = false) String password,
		@RequestParam(required = false) String captcha,
		@RequestParam(required = false) MultipartFile avatar) {

		logger.trace ' -- 添加用户注册记录 -- '

		if (!validatePhone(phone)) {
			return '{"success" : "0", "message": "err003"}'
		}

		if (!validatePwd(password)) {
			return '{"success": "0", "message": "err004"}'
		}

		userService.register(phone, password, captcha, avatar)
	}

	/**
	 * @author blues
	 * @param phone 用户手机号
	 * @return 判断手机用户，不存在创建，存在给用户手机发一条验证码短信
	 */
	@RequestMapping(value = 'sendCaptcha', method = RequestMethod.GET)
	sendCaptcha(@RequestParam(required = false) String phone) {

		logger.trace ' -- 发送验证码 -- '
		logger.trace "phone := $phone"

		if (!validatePhone(phone)) { // 验证用户手机号是否无效
			return '{"success" : "0", "message": "err003"}'
		}

		userService.sendCaptcha(phone)

	}

	/**
	 * 忘记密码重置
	 * @param phone
	 * @param password
	 * @param captcha
	 * @return
	 */
	@RequestMapping(value = 'resetPassword', method = RequestMethod.POST)
	resetPassword(@RequestParam(required = false) String phone,
		@RequestParam(required = false) String password,
		@RequestParam(required = false) String captcha) {

		logger.trace(" -- 忘记密码重置 -- ")

		if (StringUtils.isEmpty(captcha)) {
			return '{"success": "0", "message": "err007"}'
		}

		if (!validatePhone(phone)) {
			return '{"success" : "0", "message": "err003"}'
		}

		if (!validatePwd(password)) {
			return '{"success": "0", "message": "err004"}'
		}

		userService.resetPassword(phone, password, captcha)

	}

	/**
	 * 修改用户
	 */
	@Override
	@RequestMapping(method = RequestMethod.PUT)
	modify(@RequestBody User user) {

	userService.modify(user)

	}

}