package com.knowheart3.controller.user
import com.knowheart3.repository.booking.BookingRepository
import com.knowheart3.repository.doctor.DoctorInfoRepository
import com.knowheart3.repository.user.*
import com.knowheart3.service.SmsService
import com.knowheart3.service.user.UserService
import com.knowheart3.utils.DateCommons
import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.user.*
import com.qubaopen.survey.entity.user.User.ThirdType
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.lang3.RandomStringUtils
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.time.DateFormatUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

import static com.knowheart3.utils.ValidateUtil.validatePhone
import static com.knowheart3.utils.ValidateUtil.validatePwd
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

	@Autowired
	UserLogRepository userLogRepository
	
	@Autowired
	UserInfoRepository userInfoRepository
	
	@Autowired
	UserGoldRepository userGoldRepository
	
	@Autowired
	UserUDIDRepository userUDIDRepository
	
	@Autowired
	UserThirdRepository userThirdRepository

    @Autowired
    UserCaptchaRepository userCaptchaRepository
	
	@Autowired
	SmsService smsService

    @Autowired
    BookingRepository bookingRepository

    @Autowired
    DoctorInfoRepository doctorInfoRepository

	@Value('${whitePassword}')
	String whitePassword

	@Override
	protected MyRepository<User, Long> getRepository() {
		userRepository
	}

	/**
	 * 用户登录
	 * @param user
	 * @return
	 */
	@RequestMapping(value = 'login', method = RequestMethod.POST)
	login(@RequestParam(required = false) String phone,
		@RequestParam(required = false) String password,
		@RequestParam(required = false) String idfa,
		@RequestParam(required = false) String udid,
		@RequestParam(required = false) String imei,
		Model model, HttpSession session) {
		/*@RequestBody User user,*/
		
		logger.trace ' -- 用户登录 -- '

		/*if (!user) {
			return '{"success" : "0", "message": "err014"}'
		}*/

		if (!validatePhone(phone)) {
			return '{"success" : "0", "message": "err003"}'
		}

		if (!validatePwd(password)) {
			return '{"success": "0", "message": "err004"}'
		}

		def loginUser = userRepository.login(phone,  DigestUtils.md5Hex(password))

		if (loginUser) {

			userService.saveUserCode(loginUser, udid, idfa, imei)
			
			def	userLog = new UserLog(
				user : loginUser,
				userLogType : new UserLogType(id : 1l),
				time : new Date()
			)
			userLogRepository.save(userLog)
			
			model.addAttribute('currentUser', loginUser)

			def userInfo = loginUser.userInfo,
			userIdCardBind = loginUser.userIdCardBind,
			userReceiveAddress = userReceiveAddressRepository.findByUserAndTrueAddress(loginUser, true)

			return  [
				'success' : '1',
				'message' : '登录成功',
				'userId' : loginUser?.id,
				'phone' : loginUser?.phone,
				'name' : userIdCardBind?.userIDCard?.name,
				'sex' : userInfo?.sex?.ordinal(),
				'nickName' : userInfo?.nickName,
				'bloodType' : userInfo?.bloodType?.ordinal(),
				'district' : '',
				'email' : loginUser?.email,
				'defaultAddress' : userReceiveAddress?.detialAddress,
				'defaultAddressId' : userReceiveAddress?.id,
				'consignee' : userReceiveAddress?.consignee,
				'defaultAddressPhone' : userReceiveAddress?.phone,
				'idCard' : userIdCardBind?.userIDCard?.IDCard,
				'birthday' : userInfo?.birthday,
				'avatarPath' : userInfo?.avatarPath,
				'signature' : userInfo?.signature
			]
		}

		'{"success" : "0", "message": "亲，您输入的帐号或密码有误哟！"}'
	}
    /**
     * 用户登录
     * @param user
     * @return
     */
    @RequestMapping(value = 'captchaLogin', method = RequestMethod.POST)
    captchaLogin(@RequestParam(required = false) String phone,
          @RequestParam(required = false) String captcha,
          @RequestParam(required = false) Integer loginType,
          @RequestParam(required = false) String idfa,
          @RequestParam(required = false) String udid,
          @RequestParam(required = false) String imei,
          Model model, HttpSession session) {
        /*@RequestBody User user,*/

        logger.trace ' -- 用户登录 -- '

        if (!validatePhone(phone)) {
            return '{"success" : "0", "message": "err003"}'
        }
		def loginUser = userRepository.findByPhone(phone)
		if (!loginUser) {
			return '{"success" : "0", "message": "err001"}'
		}
//		if ((captcha && captcha != whitePassword) && (loginUser.password != DigestUtils.md5Hex("knowheart$captcha"))) {
//			return '{"success" : "0", "message": "err002"}'
//		}
		if (loginUser.password != DigestUtils.md5Hex("knowheart$captcha")) {
			return '{"success" : "0", "message": "err002"}'
		}

		def now = new Date()
		if (loginUser.loginDate == null) {
			loginUser.loginDate = now
		}
		def diffDay = (now.time - loginUser.loginDate.time) / 24 / 3600 / 1000
		if (diffDay > 3650) {
			return '{"success" : "0", "message" : "亲,登陆超时～请重新登陆哦～"}'
		}
		def userCaptcha = userCaptchaRepository.findOne(loginUser.id)

		userCaptcha.captcha = null
		userCaptchaRepository.save(userCaptcha)

		userService.saveUserCode(loginUser, udid, idfa, imei)

		def	userLog = new UserLog(
				user : loginUser,
				time : now
		)

		if (null == loginType) {
			userLog.userLogType = new UserLogType(id : 1l)
			loginUser.loginDate = now
		} else {
			def typeId = loginType as Long
			userLog.userLogType = new UserLogType(id : typeId)
		}

		userLogRepository.save(userLog)

		model.addAttribute('currentUser', loginUser)

		def userInfo = loginUser.userInfo,
			userIdCardBind = loginUser.userIdCardBind,
			userReceiveAddress = userReceiveAddressRepository.findByUserAndTrueAddress(loginUser, true)

		return  [
				'success'            : '1',
				'message'            : '登录成功',
				'userId'             : loginUser?.id,
				'phone'              : loginUser?.phone,
				'name'               : userIdCardBind?.userIDCard?.name,
				'sex'                : userInfo?.sex?.ordinal(),
				'nickName'           : userInfo?.nickName,
				'bloodType'          : userInfo?.bloodType?.ordinal(),
				'district'           : '',
				'email'              : loginUser?.email,
				'defaultAddress'     : userReceiveAddress?.detialAddress,
				'defaultAddressId'   : userReceiveAddress?.id,
				'consignee'          : userReceiveAddress?.consignee,
				'defaultAddressPhone': userReceiveAddress?.phone,
				'idCard'             : userIdCardBind?.userIDCard?.IDCard,
				'birthday'           : userInfo?.birthday,
				'avatarPath'         : userInfo?.avatarPath,
				'signature'          : userInfo?.signature
		]
    }

	/**
	 * 第三方登陆
	 * @param token
	 * @param nickName
	 * @param avatarUrl
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping(value = 'thirdLogin', method = RequestMethod.POST)
	@Transactional
	thirdLogin(@RequestParam String token,
		@RequestParam(required = false) String nickName,
		@RequestParam(required = false) String avatarUrl,
		@RequestParam(required = false) Integer type,
		@RequestParam(required = false) String idfa,
		@RequestParam(required = false) String udid,
		@RequestParam(required = false) String imei,
		Model model, HttpSession session) {
		
		def userThird, user, userInfo
		if (type == null) {
			return '{"success" : "0", "message" : "亲，平台出错啦"}'
		}
		// 通过token和类型查找用户
//		user = userRepository.findByTokenAndThirdType(token, ThirdType.values()[type])
		user = userRepository.thirdLogin(token, ThirdType.values()[type])

		def newUser = false
		// 第一次登陆
		if (!user) {
			newUser = true

			user = new User(
					token: token,
					thirdType: ThirdType.values()[type],
					activated: true,
					loginDate: new Date()
			)
			user = userRepository.save(user)

			def tempName = ''
			switch (type) {
				case 0 : tempName = "微博用户${RandomStringUtils.random(1, '123456789') + RandomStringUtils.randomNumeric(5)}"
					break
				case 1 : tempName = "微信用户${RandomStringUtils.random(1, '123456789') + RandomStringUtils.randomNumeric(5)}"
					break
				case 2 : tempName = "QQ用户${RandomStringUtils.random(1, '123456789') + RandomStringUtils.randomNumeric(5)}"
					break
			}
			
			userInfo = new UserInfo(
				id : user.id,
				nickName : tempName,
				avatarPath : avatarUrl,
				publicAnswersToFriend : true
			)
			def userUdid = new UserUDID(
				id : user.id,
				idfa : idfa,
				udid : udid,
				imei : imei,
				push : true,
				startTime : DateCommons.String2Date('09:00','HH:mm'),
				endTime : DateCommons.String2Date('22:00','HH:mm')
			)
			def userGold = new UserGold(
				id : user.id
			)
			userThird = new UserThird(
				id : user.id,
				token : token,
				nickName : nickName,
				avatarUrl : avatarUrl,
				thirdType : type
			)
			userThirdRepository.save(userThird)
			userInfoRepository.save(userInfo)
			userGoldRepository.save(userGold)
			userUDIDRepository.save(userUdid)
		} else {
			if (nickName || avatarUrl) {
				userThird = userThirdRepository.findOne(user.id)
				user.loginDate = new Date()
				if (nickName) {
					userThird.nickName = nickName
				}
				if (avatarUrl) {
					userThird.avatarUrl = avatarUrl
				}
				userThirdRepository.save(userThird)
			}
			userInfo = user.userInfo
		}
		model.addAttribute('currentUser', user)
		
		def userIdCardBind = user.userIdCardBind,
			userReceiveAddress = userReceiveAddressRepository.findByUserAndTrueAddress(user, true)
		userService.saveUserCode(user, udid, idfa, imei)
		
		return  [
				'success'            : '1',
				'message'            : '登录成功',
				'userId'             : user?.id,
				'phone'              : user?.phone,
				'name'               : user?.userIdCardBind?.userIDCard?.name,
				'sex'                : userInfo?.sex?.ordinal(),
				'nickName'           : userInfo?.nickName,
				'bloodType'          : userInfo?.bloodType?.ordinal(),
				'district'           : '',
				'email'              : user?.email,
				'defaultAddress'     : userReceiveAddress?.detialAddress,
				'defaultAddressId'   : userReceiveAddress?.id,
				'consignee'          : userReceiveAddress?.consignee,
				'defaultAddressPhone': userReceiveAddress?.phone,
				'idCard'             : user?.userIdCardBind?.userIDCard?.IDCard,
				'birthday'           : userInfo?.birthday,
				'avatarPath'         : userInfo?.avatarPath,
				'signature'          : userInfo?.signature,
				'newUser'            : newUser
		]
		
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
			@RequestParam(required = false) MultipartFile avatar,
			HttpServletRequest request
	) {

		logger.trace ' -- 添加用户注册记录 -- '

		if (!validatePhone(phone)) {
			return '{"success" : "0", "message": "err003"}'
		}

		if (!validatePwd(password)) {
			return '{"success": "0", "message": "err004"}'
		}
		if (!StringUtils.isNotEmpty(captcha)) {
			return '{"success": "0", "message": "err012"}'
		}

		userService.register(phone, password, captcha, avatar, request)
	}

	/**
	 * 判断手机用户，不存在创建，存在给用户手机发一条验证码短信
	 * @param phone
	 * @param activated
	 * @return
	 */
	@RequestMapping(value = 'sendCaptcha', method = RequestMethod.GET)
	sendCaptcha(@RequestParam(required = false) String phone, @RequestParam(required = false) Boolean activated) {

		logger.trace ' -- 发送验证码 -- '
		logger.trace "phone := $phone"

		if (!validatePhone(phone)) {
			// 验证用户手机号是否无效
			return '{"success" : "0", "message": "err003"}'
		}
		
		// true 忘记密码判断， false 新用户注册判断
		if (activated == null) {
			activated = false
		}

		userService.sendCaptcha(phone, activated)
	}

    /**
     * @param phone 用户手机号
     * @return 判断手机用户，不存在创建，存在给用户手机发一条验证码短信
     */
    @RequestMapping(value = 'sendLoginCaptcha', method = RequestMethod.GET)
    sendLoginCaptcha(@RequestParam(required = false) String phone) {

        logger.trace ' -- 发送验证码 -- '
        logger.trace "phone := $phone"

        if (!validatePhone(phone)) {
            // 验证用户手机号是否无效
            return '{"success" : "0", "message": "err003"}'
        }

        userService.sendLoginCaptcha(phone)
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
		
		def u = userRepository.findByPhone(phone)
		
		if (!u.activated) {
			return '{"success": "0", "message": "err019"}'
		}
		
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

	/**
	 * 修改密码
	 * @param oldPwd
	 * @param newPwd
	 * @param user
	 * @return
	 */
	@RequestMapping(value = 'modifyPassword', method = RequestMethod.POST)
	modifyPassword(@RequestParam(required = false) String oldPwd, @RequestParam(required = false) String newPwd, @ModelAttribute('currentUser') User user) {

		if (!validatePwd(oldPwd)) {
			return '{"success" : "0", "message" : "err015"}'
		}
		if (!StringUtils.equals(DigestUtils.md5Hex(oldPwd), user.password)) {
			return '{"success" : "0", "message" : "err016"}'
		}
		if (!validatePwd(newPwd)) {
			return '{"success": "0", "message": "err004"}'
		}
		user.password = DigestUtils.md5Hex(newPwd)
		userRepository.save(user)
		'{"success" : "1"}'
	}
	@RequestMapping(value = 'refreshToken', method = RequestMethod.GET)
	refreshToken() {
		smsService.refreshToken()
		
	}
	/**
	 * @param doctor
	 * @param request
	 * @return
	 * 退出
	 */
	@RequestMapping(value = 'logout', method = RequestMethod.GET)
	logout(@ModelAttribute('currentUser') User user, Model model) {

		user = new User()
		model.addAttribute("currentUser", user)

//		session.invalidate()
		'{"success" : "1"}'
	}

	/**
	 * 绑定手机
	 * @param phone
	 * @param captcha
	 * @param user
	 */
	@RequestMapping(value = 'bindingPhone')
	bindingPhone(@RequestParam String phone, @RequestParam String captcha,
				 @ModelAttribute('currentUser') User user) {

		if (!validatePhone(phone)) {
			return '{"success" : "0", "message": "err003"}'
		}
		def userCaptcha = userCaptchaRepository.findOne(user.id)
		if (StringUtils.equals(userCaptcha.captcha, captcha)) {
			return '{"success" : "0", "message": "err007"}'
		}


	}


    @RequestMapping(value = 'testMessage', method = RequestMethod.GET)
    testMessage() {

        def di = doctorInfoRepository.findOne(1l)


		StringBuffer param = new StringBuffer();
		param.append("{\"name\" : \"" + "112121" + "\"");
		param.append(", \"qq\" : \"" + (di != null ? di.getQq() : "") + "\"");
		param.append(", \"time\" : \"" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss") + "\"}");

		System.out.println(param.toString());
        Map<String, Object> map2 =  smsService.sendSmsMessage('13621673989', 9, param.toString())

		map2
    }

}