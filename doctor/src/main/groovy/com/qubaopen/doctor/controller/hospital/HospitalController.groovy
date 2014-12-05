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
import com.qubaopen.doctor.repository.hospital.HospitalLogRepository
import com.qubaopen.doctor.repository.hospital.HospitalRepository
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
	
	@Override
	MyRepository<Hospital, Long> getRepository() {
		hospitalRepository
	}

	
	/**
	 * 医师登录
	 * @param user
	 * @return
	 */
	@RequestMapping(value = 'login', method = RequestMethod.POST)
	login(@RequestParam(required = false) String email,
		@RequestParam(required = false) String password,
		@RequestParam(required = false) String idfa,
		@RequestParam(required = false) String udid,
		@RequestParam(required = false) String imei,
		Model model, HttpSession session) {
		
		logger.trace ' -- 医师登录 -- '

		if (!validatePwd(password)) {
			return '{"success": "0", "message": "err004"}'
		}

		def loginHospital = hospitalRepository.login(email,  DigestUtils.md5Hex(password))

		if (loginHospital) {

			hospitalService.saveUserCode(loginHospital, udid, idfa, imei)
			
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
					recordPaths << it.doctorRecordPath
				}

			return  [
				'success' : '1',
				'message' : '登录成功',
				'doctorId' : loginHospital?.id,
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
				'recordPaths' : recordPaths.join(',')
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
			@RequestParam(required = false) String captcha) {

		logger.trace('-- register --')
		
		if (!validateEmail(email)) {
			return '{"success" : "0", "message": "err005"}'
		}

		if (!validatePwd(password)) {
			return '{"success": "0", "message": "err004"}'
		}
		
		if (!StringUtils.isNotEmpty(captcha)) {
			return '{"success": "0", "message": "err012"}'
		}
		hospitalService.register(email, password, captcha)
	}
			
	/**
	 * @param phone
	 * @param activated
	 * @return
	 * 发送验证码
	 */
	@RequestMapping(value = 'sendCaptcha', method = RequestMethod.GET)
	sendCaptcha(@RequestParam(required = false) String email, @RequestParam(required = false) Boolean activated) {
		
		logger.trace ' -- 发送验证码 -- '
		logger.trace "phone := $email"

		if (!validateEmail(email)) {
			// 验证用户手机号是否无效
			return '{"success" : "0", "message": "err005"}'
		}
		
		// true 忘记密码判断， false 新用户注册判断
		if (activated == null) {
			activated = false
		}

		hospitalService.sendCaptcha(email, activated)
		
	}
	
	/**
	 * 忘记密码重置
	 * @param phone
	 * @param password
	 * @param captcha
	 * @return
	 */
	@RequestMapping(value = 'resetPassword', method = RequestMethod.POST)
	resetPassword(@RequestParam(required = false) String email,
			@RequestParam(required = false) String password,
			@RequestParam(required = false) String captcha) {

		logger.trace(" -- 忘记密码重置 -- ")
		
		def h = hospitalRepository.findByEmail(email)
		
		if (!h.activated) {
			return '{"success": "0", "message": "err019"}'
		}
		
		if (StringUtils.isEmpty(captcha)) {
			return '{"success": "0", "message": "err007"}'
		}

		if (!validateEmail(email)) {
			return '{"success" : "0", "message": "err005"}'
		}
		
		if (!validatePwd(password)) {
			return '{"success": "0", "message": "err004"}'
		}

		hospitalService.resetPassword(h, password, captcha)
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
	
	@RequestMapping(value = 'logout', method = RequestMethod.GET)
	logout(@ModelAttribute('currentDoctor') Doctor doctor, HttpServletRequest request) {
		
		def session = request.getSession()
		
		session.removeAttribute('currentDoctor')
		
		'{"success" : "1"}'
	}
	
}
