package com.qubaopen.doctor.controller.hospital;


import static com.qubaopen.doctor.utils.ValidateUtil.*

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.doctor.repository.hospital.HospitalCaptchaRepository;
import com.qubaopen.doctor.repository.hospital.HospitalLogRepository
import com.qubaopen.doctor.repository.hospital.HospitalRepository
import com.qubaopen.doctor.service.CaptchaService;
import com.qubaopen.doctor.service.HospitalService
import com.qubaopen.survey.entity.doctor.Doctor
import com.qubaopen.survey.entity.hospital.Hospital
import com.qubaopen.survey.entity.hospital.HospitalLog
import com.qubaopen.survey.entity.user.UserLogType

@RestController
@RequestMapping('uHospital')
@SessionAttributes('currentHospital')
public class HospitalController extends AbstractBaseController<Hospital, Long> {

	@Autowired
	HospitalRepository hospitalRepository
	
	@Autowired
	HospitalLogRepository hospitalLogRepository
	
	@Autowired
	HospitalService hospitalService
	
	@Autowired
	HospitalCaptchaRepository hospitalCaptchaRepository
	
	@Autowired
	CaptchaService captchaService
	@Override
	MyRepository<Hospital, Long> getRepository() {
		hospitalRepository
	}

	
	/**
	 * 诊所登录
	 * @param user
	 * @return
	 */
	@RequestMapping(value = 'login', method = RequestMethod.POST)
	login(@RequestParam(required = false) String email,
		@RequestParam(required = false) String password,
//		@RequestParam(required = false) String idfa,
//		@RequestParam(required = false) String udid,
//		@RequestParam(required = false) String imei,
		Model model, HttpSession session) {
		
		logger.trace ' -- 诊所登录 -- '

		if (!validatePwd(password)) {
			return '{"success": "0", "message": "err004"}'
		}

		def loginHospital = hospitalRepository.login(email,  DigestUtils.md5Hex(password))

		if (loginHospital) {

//			hospitalService.saveUserCode(loginHospital, udid, idfa, imei)
			
			def	hospitalLog = new HospitalLog(
				hospital : loginHospital,
				userLogType : new UserLogType(id : 1l)
			)
			hospitalLogRepository.save(hospitalLog)
			
			model.addAttribute('currentHospital', loginHospital)

			def hospitalInfo = loginHospital.hospitalInfo,
				records = hospitalInfo.hospitalDoctorRecords,
				recordPaths = []
				records.each {
					recordPaths << [
						'pathId' : it.id,
						'path' : it.doctorRecordPath
					]
				}

			return  [
				'success' : '1',
				'message' : '登录成功',
				'hospitalId' : loginHospital?.id,
				'email' : loginHospital?.email,
				'address' : hospitalInfo?.address,
				'establishTime' : hospitalInfo?.establishTime,
				'bookTime' : hospitalInfo?.bookTime,
				'phone' : hospitalInfo?.phone,
				'urgentPhone' : hospitalInfo?.urgentPhone,
				'qq' : hospitalInfo?.qq,
				'introduce' : hospitalInfo?.introduce,
				'wordsConsult' : hospitalInfo?.wordsConsult,
				'minCharge' : hospitalInfo?.minCharge,
				'maxCharge' : hospitalInfo?.maxCharge,
				'recordPaths' : recordPaths
			]
		}

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
	register(@RequestParam(required = false) String email,
			@RequestParam(required = false) String password,
			HttpServletRequest request) {

		logger.trace('-- register --')
		
		if (!validateEmail(email)) {
			return '{"success" : "0", "message": "err005"}'
		}

		if (!validatePwd(password)) {
			return '{"success": "0", "message": "err004"}'
		}
		
//		if (!StringUtils.isNotEmpty(captcha)) {
//			return '{"success": "0", "message": "err012"}'
//		}
		
		
//		def url = "{$request.getServerName()}:{$request.getServerPort()}"
		
//		def url = "${request.getServletContext().getRealPath('/')}pic/$filename"
		
		def url = "http://" + request.getServerName() + ":" + request.getServerPort() + "/doctor/uHospital/"
		
		hospitalService.register(url, email, password)
	}
			
	/**
	 * @param phone
	 * @param activated
	 * @return
	 * 发送验证码
	 */
	@RequestMapping(value = 'sendCaptcha', method = RequestMethod.GET)
	sendCaptcha(@RequestParam(required = false) String email) {
		
		logger.trace ' -- 发送验证码 -- '

		if (!validateEmail(email)) { // 验证邮箱格式
			return '{"success" : "0", "message": "err005"}'
		}
		
		hospitalService.sendCaptcha(email)
		
	}
	
	/**
	 * @param email
	 * @param captcha
	 * @return
	 * 校验验证码
	 */
	@RequestMapping(value = 'verifyCaptcha', method = RequestMethod.POST)
	verifyCaptcha(@RequestParam String email, @RequestParam String captcha) {
		
		logger.trace '-- 校验验证码 --'
		
		if (!validateEmail(email)) {
			return '{"success" : "0", "message": "err005"}'
		}
		
		if (StringUtils.isEmpty(captcha)) {
			return '{"success": "0", "message": "err007"}'
		}

		def hospital = hospitalRepository.findByEmailAndActivated(email, true)
		
		if (!hospital) {
			return '{"success" : "0", "message" : "err001"}' // 没有用户
		}
		def hCaptcha = hospitalCaptchaRepository.findOne(hospital.id)
		if (hCaptcha.captcha != captcha) {
			return '{"success" : "0", "message" : "err007"}' // 验证码不正确
		}
		def now = new Date()
		if ((now.time - hCaptcha.lastSentDate.time) > 900000) {
			return '{"success" : "0", "message" : "err023"}' // 15mins 超时
		}
		
		hCaptcha.captcha = null
		hospitalCaptchaRepository.save(hCaptcha)
		[
			'success' : '1',
			'hospitalId' : hospital?.id	
		]		
	}
	
	/**
	 * 忘记密码重置
	 * @param phone
	 * @param password
	 * @param captcha
	 * @return
	 */
	@RequestMapping(value = 'resetPassword', method = RequestMethod.POST)
	resetPassword(@RequestParam long id,
			@RequestParam(required = false) String password) {

		logger.trace(" -- 忘记密码重置 -- ")
		
		def h = hospitalRepository.findOne(id)
		
		if (!h.activated) {
			return '{"success": "0", "message": "err019"}'
		}
		
		if (!validatePwd(password)) {
			return '{"success": "0", "message": "err004"}'
		}

		hospitalService.resetPassword(h, password)
	}
			
	/**
	 * 修改密码
	 * @param oldPwd
	 * @param newPwd
	 * @param user
	 * @return
	 */
	@RequestMapping(value = 'modifyPassword', method = RequestMethod.POST)
	modifyPassword(@RequestParam(required = false) String oldPwd, @RequestParam(required = false) String newPwd, @ModelAttribute('currentHospital') Hospital hospital) {

		if (!validatePwd(oldPwd)) {
			return '{"success" : "0", "message" : "err015"}'
		}
		if (!StringUtils.equals(DigestUtils.md5Hex(oldPwd), hospital.password)) {
			return '{"success" : "0", "message" : "err016"}'
		}
		if (!validatePwd(newPwd)) {
			return '{"success": "0", "message": "err004"}'
		}
		hospital.password = DigestUtils.md5Hex(newPwd)
		hospitalRepository.save(hospital)
		'{"success" : "1"}'
	}
	
	/**
	 * @param doctor
	 * @param request
	 * @return
	 * 退出
	 */
	@RequestMapping(value = 'logout', method = RequestMethod.GET)
	logout(@ModelAttribute('currentHospital') Hospital hospital, HttpServletRequest request) {
		
		def session = request.getSession()
		session.invalidate()
		
		'{"success" : "1"}'
	}
	
	@RequestMapping(value = 'activateAccount', method = RequestMethod.GET)
	activateAccount(@RequestParam long id, @RequestParam String captcha) {
		
		logger.trace '-- 激活账户 --'
		def hCaptcha = hospitalCaptchaRepository.findOne(id)
		
		if (hCaptcha.captcha != captcha) {
			return '{"success" : "0", "message" : "err022 验证码过期"}' //验证码过期
		}
		def now = new Date()
		if (now.time - hCaptcha.lastSentDate.time > 900000) {
			return '{"success : "0", "message" : "err023 该链接已超时"}' // 超时
		}
		def hospital = hospitalRepository.findOne(id)
		
		hospital.activated = true
		hCaptcha.captcha = null
		hospitalRepository.save(hospital)
		hospitalCaptchaRepository.save(hCaptcha)
		'{"success" : "1"}'
	}

}
