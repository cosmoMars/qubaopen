package com.qubaopen.doctor.controller.doctor;

import static com.qubaopen.doctor.utils.ValidateUtil.*

import javax.servlet.http.HttpSession

import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.lang3.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttributes

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.doctor.repository.doctor.DoctorAddressRepository;
import com.qubaopen.doctor.repository.doctor.DoctorLogRepository;
import com.qubaopen.doctor.repository.doctor.DoctorRepository;
import com.qubaopen.doctor.service.DoctorService
import com.qubaopen.survey.entity.doctor.Doctor
import com.qubaopen.survey.entity.doctor.DoctorLog
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.user.UserLogType


@RestController
@RequestMapping('uDoctor')
@SessionAttributes('currentDoctor')
public class DoctorController extends AbstractBaseController<Doctor, Long> {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass())

	@Autowired
	DoctorRepository doctorRepository
	
	@Autowired
	DoctorService doctorService
	
	@Autowired
	DoctorLogRepository doctorLogRepository
	
	@Autowired
	DoctorAddressRepository doctorAddressRepository
	

	@Override
	protected MyRepository<Doctor, Long> getRepository() {
		return doctorRepository
	}

	/**
	 * 医师登录
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
		
		logger.trace ' -- 医师登录 -- '

		if (!validatePhone(phone)) {
			return '{"success" : "0", "message": "err003"}'
		}

		if (!validatePwd(password)) {
			return '{"success": "0", "message": "err004"}'
		}

		def loginDoctor = doctorRepository.login(phone,  DigestUtils.md5Hex(password))

		if (loginDoctor) {

			doctorService.saveUserCode(loginDoctor, udid, idfa, imei)
			
			def	doctorLog = new DoctorLog(
				doctor : loginDoctor,
				userLogType : new UserLogType(id : 1l)
			)
			doctorLogRepository.save(doctorLog)
			
			model.addAttribute('currentDoctor', loginDoctor)

			def doctorInfo = loginDoctor.doctorInfo,
			doctorIdCardBind = loginDoctor.doctorIdCardBind,
			doctorAddress = doctorAddressRepository.findByDoctorAndUsed(loginDoctor, true)

			return  [
				'success' : '1',
				'message' : '登录成功',
				'doctorId' : loginDoctor?.id,
				'phone' : loginDoctor?.phone,
				'email' : loginDoctor?.email,
				'name' : doctorIdCardBind?.userIDCard?.name,
				'sex' : doctorInfo?.sex?.ordinal(),
				'email' : loginDoctor?.email,
				'address' : doctorAddress?.address,
				'addressId' : doctorAddress?.id,
//				'consignee' : doctorAddress?.consignee,
//				'defaultAddressPhone' : doctorAddress?.phone,
				'idCard' : doctorIdCardBind?.userIDCard?.IDCard,
				'birthday' : doctorInfo?.birthday,
				'experience' : doctorInfo?.experience,
				'field' : doctorInfo?.field,
				'qq' : doctorInfo?.qq,
				'consultType' : doctorInfo?.consultType?.ordinal(),
				'targetUser' : doctorInfo?.targetUser,
				'genre' : doctorInfo?.genre,
				'time' : doctorInfo?.time,
				'quick' : doctorInfo?.quick,
				'introduce' : doctorInfo?.introduce,
				'recordPath' : doctorInfo?.recordPath,
				'avatarPath' : doctorInfo?.avatarPath
			]
		}

//		'{"success" : "0", "message": "亲，您输入的帐号或密码有误哟！"}'
		'{"success" : "0", "message": "err018"}'
	}
		
	/**
	 * @param phone
	 * @param password
	 * @param captcha
	 * @return
	 * 医师注册
	 */
	@RequestMapping(value = 'register', method = RequestMethod.POST)
	register(@RequestParam(required = false) String phone,
			@RequestParam(required = false) String password,
			@RequestParam(required = false) String captcha) {

		logger.trace('-- register --')
		
		if (!validatePhone(phone)) {
			return '{"success" : "0", "message": "err003"}'
		}

		if (!validatePwd(password)) {
			return '{"success": "0", "message": "err004"}'
		}
		
		if (!StringUtils.isNotEmpty(captcha)) {
			return '{"success": "0", "message": "err012"}'
		}
		doctorService.register(phone, password, captcha)
	}
			
	/**
	 * @param phone
	 * @param activated
	 * @return
	 * 发送验证码
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
			activated == false
		}

		doctorService.sendCaptcha(phone, activated)
		
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
		
		def d = doctorRepository.findByPhone(phone)
		
		if (!d.activated) {
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

		doctorService.resetPassword(d, password, captcha)
	}
			
	/**
	 * 修改密码
	 * @param oldPwd
	 * @param newPwd
	 * @param user
	 * @return
	 */
	@RequestMapping(value = 'modifyPassword', method = RequestMethod.POST)
	modifyPassword(@RequestParam(required = false) String oldPwd, @RequestParam(required = false) String newPwd, @ModelAttribute('currentDoctor') Doctor doctor) {

		if (!validatePwd(oldPwd)) {
			return '{"success" : "0", "message" : "err015"}'
		}
		if (!StringUtils.equals(DigestUtils.md5Hex(oldPwd), doctor.password)) {
			return '{"success" : "0", "message" : "err016"}'
		}
		if (!validatePwd(newPwd)) {
			return '{"success": "0", "message": "err004"}'
		}
		doctor.password = DigestUtils.md5Hex(newPwd)
		doctorRepository.save(doctor)
		'{"success" : "1"}'
	}

}
