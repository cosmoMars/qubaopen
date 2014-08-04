package com.qubaopen.survey.controller.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.repository.user.UserRepository
import com.qubaopen.survey.service.user.UserService

/**
 * @author mars 用户表
 */
@RestController
@RequestMapping('users')
class UserController extends AbstractBaseController<User, Long> {

	@Autowired
	UserRepository userRepository

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

		userService.login(user)

	}

	/**
	 * 添加用户注册记录
	 * @param user
	 * @return
	 */
	@RequestMapping(value ='register', method = RequestMethod.POST, consumes = 'multipart/form-data')
	register(@RequestParam String phone, @RequestParam String password, @RequestParam String captcha, @RequestParam MultipartFile avatar) {

		logger.trace ' -- 添加用户注册记录 -- '

		userService.register(phone, password, captcha, avatar)
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
	resetPassword(@RequestParam long userId, @RequestParam String password, @RequestParam String captcha) {

		logger.trace(" -- 忘记密码重置 -- ")

		userService.resetPassword(userId, password, captcha)

	}

}