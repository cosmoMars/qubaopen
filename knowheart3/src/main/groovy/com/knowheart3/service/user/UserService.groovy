package com.knowheart3.service.user
import com.knowheart3.repository.reward.RewardActivityRecordRepository
import com.knowheart3.repository.user.*
import com.knowheart3.service.SmsService
import com.knowheart3.service.self.SelfService
import com.knowheart3.utils.DateCommons
import com.knowheart3.utils.UploadUtils
import com.qubaopen.survey.entity.user.*
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.lang3.RandomStringUtils
import org.apache.commons.lang3.time.DateFormatUtils
import org.apache.commons.lang3.time.DateUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

import javax.servlet.http.HttpServletRequest

import static com.knowheart3.utils.ValidateUtil.validateEmail
import static com.knowheart3.utils.ValidateUtil.validatePwd

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
	UserCaptchaLogRepository userCaptchaLogRepository

	@Autowired
	UserReceiveAddressRepository userReceiveAddressRepository

	@Autowired
	UserIDCardBindRepository userIDCardBindRepository

	@Autowired
	UserGoldRepository userGoldRepository

	@Autowired
	SmsService smsService
	
	@Autowired
	SelfService selfService
	
	@Autowired
	UserMoodRepository userMoodRepository

    @Value('${user_url}')
    String user_url
	
	@Autowired
	RewardActivityRecordRepository rewardActivityRecordRepository

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
				'success' : "1",
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

		'{"success" : "0", "message": "亲，您输入的帐号或密码有误哟！"}'
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
	register(String phone, String password, String captcha, MultipartFile avatar, HttpServletRequest request) {

		def u = userRepository.findByPhone(phone)
		if (u) {
			if (u.activated) {
				return '{"success": "0", "message": "err006"}'
			}
			def userCaptchaLog = new UserCaptchaLog(
				user : u,
				captcha : captcha,
				action : '1'
			)
			userCaptchaLogRepository.save(userCaptchaLog)
			def userCaptcha = userCaptchaRepository.findOne(u.id)
			if (userCaptcha?.captcha != captcha) {
				return '{"success": "0", "message": "err007"}'
			}
			userCaptcha.captcha = null
			userCaptchaRepository.save(userCaptcha)
			
			u.password = DigestUtils.md5Hex(password)
			u.activated = true
			saveUserAndUserAvatar(u, avatar, request)

			return [
				'success': '1',
				'userId' : u.id
			]
		}

		'{"success": "0", "message": "err008"}'
	}


	/**
	 * 发送验证码
	 * @param phone
	 * @return
	 */
	@Transactional
	sendCaptcha(String phone, activated) {

		def user
		if (!activated) { //新用户
			user = userRepository.findByPhone(phone)
			if (user && user.activated) {
				return '{"success" : "0", "message" : "err006"}'
			}
		} else { // 忘记密码发送短信
			user = userRepository.findByPhoneAndActivated(phone, activated)
			if (!user) {
				return '{"success" : "0", "message" : "err001"}'
			}
		}
		
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
				return '{"success": "0", "message": "err009"}'
			}

			if (DateUtils.isSameDay(today, userCaptcha.lastSentDate) && userCaptcha.sentNum > 10) {
				return '{"success": "0", "message": "err010"}'
			}
		}
		def captcha
		
		if (userCaptcha && userCaptcha.captcha) {
			captcha = userCaptcha.captcha
		} else {
			// 生成6位数字格式的验证码
			captcha = RandomStringUtils.random(1, '123456789') + RandomStringUtils.randomNumeric(5)
		}

        // 给指定的用户手机号发送6位随机数的验证码
		def result = smsService.sendCaptcha(phone, captcha)
		
		def userCaptchaLog = new UserCaptchaLog(
			user : user,
			captcha : captcha,
			status : result.get('resCode'),
			action : '0'
		)
		userCaptchaLogRepository.save(userCaptchaLog)
		
		if (!result.get('isSuccess')) {
			return '{"success": "0", "message": "err011"}'
		}

		if (userCaptcha) {
			if (DateUtils.isSameDay(today, userCaptcha.lastSentDate)) {
				userCaptcha.sentNum ++
			} else {
				userCaptcha.sentNum = 1
			}
			userCaptcha.captcha = captcha
			userCaptcha.lastSentDate = today
		} else {
			userCaptcha = new UserCaptcha(
				id : user.id,
				captcha : captcha,
				lastSentDate : today,
				sentNum : 1
			)
		}

		userCaptchaRepository.save(userCaptcha)
		'{"success": "1"}'
	}
    /**
     * 发送验证码
     * @param phone
     * @return
     */
    @Transactional
    sendLoginCaptcha(String phone) {

        def user = userRepository.findByPhone(phone)

        if (!user) {
            user = new User(
                    phone : phone,
					userName: "新用户${RandomStringUtils.random(6)}"
            )
            userRepository.save(user)
        }
        def userCaptcha = userCaptchaRepository.findOne(user.id)
        def today = new Date()
        if (userCaptcha) {
            def lastSentDate = userCaptcha.lastSentDate
            if ((today.time - lastSentDate.time) < 60000) {
                return '{"success": "0", "message": "err009"}'
            }

            if (DateUtils.isSameDay(today, userCaptcha.lastSentDate) && userCaptcha.sentNum > 10) {
                return '{"success": "0", "message": "err010"}'
            }
        }
        def captcha

        if (userCaptcha && userCaptcha.captcha) {
            captcha = userCaptcha.captcha
        } else {
            // 生成6位数字格式的验证码
            captcha = RandomStringUtils.random(1, '123456789') + RandomStringUtils.randomNumeric(5)
        }

        // 给指定的用户手机号发送6位随机数的验证码
        def result = smsService.sendCaptcha(phone, captcha)

        def userCaptchaLog = new UserCaptchaLog(
                user : user,
                captcha : captcha,
                status : result.get('resCode'),
                action : '0'
        )
        userCaptchaLogRepository.save(userCaptchaLog)

        if (!result.get('isSuccess')) {
            return '{"success": "0", "message": "err011"}'
        }

        user.password = DigestUtils.md5Hex("knowheart$captcha")
        user.loginDate = today

        if (userCaptcha) {
            if (DateUtils.isSameDay(today, userCaptcha.lastSentDate)) {
                userCaptcha.sentNum ++
            } else {
                userCaptcha.sentNum = 1
            }
            userCaptcha.captcha = captcha
            userCaptcha.lastSentDate = today
        } else {
            userCaptcha = new UserCaptcha(
                    id : user.id,
                    captcha : captcha,
                    lastSentDate : today,
                    sentNum : 1
            )
        }
		if (!user.activated) {
			def userInfo = new UserInfo(
				id : user.id,
				publicAnswersToFriend : true
			)
					
			def userUdid = new UserUDID(
				id : user.id,
				push : true,
				startTime : DateCommons.String2Date('09:00','HH:mm'),
				endTime : DateCommons.String2Date('22:00','HH:mm')
			)
			def userGold = new UserGold(
				id : user.id
			)
	
			userInfoRepository.save(userInfo)
			userUDIDRepository.save(userUdid)
			userGoldRepository.save(userGold)
			user.activated = true
		}
        userRepository.save(user)
        userCaptchaRepository.save(userCaptcha)
		
        '{"success": "1"}'
    }

	/**
	 * 重置密码
	 * @param userId
	 * @param password
	 * @param captcha
	 * @return
	 */
	@Transactional
	resetPassword(String phone, String password, String captcha) {

		def u = userRepository.findByPhone(phone)
		if (!u) {
			return '{"success" : "0", "message": "err001"}'
		}

		def userCaptcha = userCaptchaRepository.findOne(u.id)
		if (!userCaptcha) {
			return '{"success": "0", "message": "err013"}'
		}

		if (captcha == userCaptcha.captcha) {
			u.password = DigestUtils.md5Hex(password)
			userRepository.save(u)
			return '{"success": "1"}'
		} else {
			return '{"success": "0", "message" : "err007"}'
		}
	}

	/**
	 * 修改用户
	 * @param user
	 * @return
	 */
	@Transactional
	modify(User user) {

		if (user.password) {
			if (!validatePwd(user.password)) {
				return '{"success" : "0", "message": "err003"}'
			}
			user.password = DigestUtils.md5Hex(user.password)
		}

		if (user.email) {
			if (!validateEmail(user.email)) {
				return '{"success": "0", "message": "err005"}'
			}
		}
		userRepository.modify(user)
		'{"success": "1"}'
	}

	/**
	 * 保存user，新建userInfo，保存头像
	 * @param user
	 * @param avatar
	 * @return
	 */
	@Transactional
	void saveUserAndUserAvatar(User user, MultipartFile avatar, HttpServletRequest request) {

		user = userRepository.save(user)

		def userInfo = null
		if (avatar != null) {
			def filename = "${user.id}_${DateFormatUtils.format(new Date(), 'yyyyMMdd-HHmmss')}.png",
			avatarPath = "${request.getServletContext().getRealPath('/')}pic/$filename"
			userInfo = new UserInfo(
				id : user.id,
				publicAnswersToFriend : true,
				avatarPath : "/pic/$filename"
			)
			saveFile(avatar.bytes, avatarPath)
		} else {
			userInfo = new UserInfo(
				id : user.id,
				publicAnswersToFriend : true
			)
		}

		def userUdid = new UserUDID(
			id : user.id,
			push : true,
			startTime : DateCommons.String2Date('09:00','HH:mm'),
			endTime : DateCommons.String2Date('22:00','HH:mm')
		)
		def userGold = new UserGold(
			id : user.id
//			currentGold : 10000 //测试使用
		)

		userInfoRepository.save(userInfo)
		userUDIDRepository.save(userUdid)
		userGoldRepository.save(userGold)
	}

    /**
     * 保存user，新建userInfo，保存头像
     * @param user
     * @param avatar
     * @return
     */
    @Transactional
    void captchaSaveUser(User user, MultipartFile avatar) {

        user = userRepository.save(user)

        if (avatar != null) {
            def name = "$user_url$user.id"
            UploadUtils.uploadTo7niu(1, name, avatar.inputStream)
        }
        def userInfo = new UserInfo(
                id : user.id,
                publicAnswersToFriend : true
        )


        def userUdid = new UserUDID(
                id : user.id,
                push : true,
                startTime : DateCommons.String2Date('09:00','HH:mm'),
                endTime : DateCommons.String2Date('22:00','HH:mm')
        )
        def userGold = new UserGold(
                id : user.id
//			currentGold : 10000 //测试使用
        )

        userInfoRepository.save(userInfo)
        userUDIDRepository.save(userUdid)
        userGoldRepository.save(userGold)
    }
	
	/**
	 * 保存udid idfa
	 * @param user
	 * @param udid
	 * @param idfa
	 * @return
	 */
	@Transactional
	saveUserCode(User user, String udid, String idfa, String imei) {
		def code = userUDIDRepository.findOne(user.id)
		if (udid) {
			code.udid = udid
		}
		if (idfa) {
			code.idfa = idfa
		}
		if (imei) {
			code.imei = imei
		}
		if (!udid && !idfa && !imei) {
			return
		}
		userUDIDRepository.save(code)
	}

	/**
	 * 获取用户首页数据，心情、性格解析度、心理指数 
	 * @param user
	 * @return
	 */
	@Transactional
	getIndexInfo(User user) {
		def userInfo = userInfoRepository.findOne(user.id)
		selfService.calcUserAnalysisRadio(userInfo)
		selfService.calcUserMentalStatus(userInfo)
		
		def userMood = userMoodRepository.findLastByTime(user)
		
		def userRewards = rewardActivityRecordRepository.findAll(
			[
				user_equal : user,
				'rewardActivity.rewardType.rewardLevel.id_equal' : 100
			]	
		)
		userInfo = userInfoRepository.findOne(user.id)
		def reslut = [
			'success' : '1',
			'moodType' : userMood?.moodType?.ordinal(),
			'lastTime' : userMood?.lastTime,
			'userSelfTitle' : userInfo?.userSelfTitle?.name,
			'deduction' : userInfo?.deduction,
			'resolution' : userInfo?.resolution,
			'message' : userMood?.message,
			'userSelfTitleId' : userInfo?.userSelfTitle?.id
			]
		
		if (userRewards != null && userRewards.size() > 0) {
			reslut << ['isJoined' : true]
		} else {
			reslut << ['isJoined' : false]
		}
		return reslut
	}
	
	private void saveFile(byte[] bytes, String filename) {
		def fos = new FileOutputStream(filename)
		fos.write(bytes)
		fos.close()
	}
	
}
