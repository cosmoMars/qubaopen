package com.qubaopen.doctor.service;

import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.lang3.RandomStringUtils
import org.apache.commons.lang3.time.DateUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.qubaopen.doctor.repository.doctor.DoctorCaptchaLogRepository;
import com.qubaopen.doctor.repository.doctor.DoctorCaptchaRepository;
import com.qubaopen.doctor.repository.doctor.DoctorInfoRepository;
import com.qubaopen.doctor.repository.doctor.DoctorRepository;
import com.qubaopen.doctor.repository.doctor.DoctorUdidRepository;
import com.qubaopen.doctor.utils.DateCommons
import com.qubaopen.survey.entity.doctor.Doctor
import com.qubaopen.survey.entity.doctor.DoctorCaptcha
import com.qubaopen.survey.entity.doctor.DoctorCaptchaLog
import com.qubaopen.survey.entity.doctor.DoctorInfo
import com.qubaopen.survey.entity.doctor.DoctorUdid

@Service
public class DoctorService {
	
	@Autowired
	DoctorRepository doctorRepository
	
	@Autowired
	DoctorCaptchaRepository doctorCaptchaRepository
	
	@Autowired
	DoctorCaptchaLogRepository doctorCaptchaLogRepository
	
	@Autowired
	SmsService smsService
	
	@Autowired
	DoctorUdidRepository doctorUdidRepository
	
	@Autowired
	DoctorInfoRepository doctorInfoRepository
	

	/**
	 * @param phone
	 * @param password
	 * @param captcha
	 * @return
	 * 医师注册
	 */
	@Transactional
	register(String phone, String password, String captcha) {
		def d = doctorRepository.findByPhone(phone)
		if (d) {
			if (d.activated) {
				return '{"success": "0", "message": "err006"}'
			}
			
			def doctorCaptchaLog = new DoctorCaptchaLog(
				doctor : d,
				captcha : captcha,
				action : '1'
			)
			doctorCaptchaLogRepository.save(doctorCaptchaLog)
			
			def doctorCaptcha = doctorCaptchaRepository.findOne(d.id)
			if (doctorCaptcha?.captcha != captcha) {
				return '{"success": "0", "message": "err007"}'
			}
			doctorCaptcha.captcha = null
			doctorCaptchaRepository.save(doctorCaptcha)
			
			d.password = DigestUtils.md5Hex(password)
			d.activated = true
			
			d = doctorRepository.save(d)
			
			def doctorInfo = new DoctorInfo(
				id : d.id,
				bookingTime : '000000000000000000000000,000000000000000000000000,000000000000000000000000,000000000000000000000000,000000000000000000000000,000000000000000000000000,000000000000000000000000'
			)
			def doctorUdid = new DoctorUdid(
				id : d.id,
				startTime : DateCommons.String2Date('09:00','HH:mm'),
				endTime : DateCommons.String2Date('22:00','HH:mm'),
				push : true
			)
			doctorInfoRepository.save(doctorInfo)
			doctorUdidRepository.save(doctorUdid)
			return [
				'success': '1',
				'doctorId' : d.id
			]
		}

		'{"success": "0", "message": "err008"}'
	}
	
	/**
	 * @param phone
	 * @param activated
	 * @return
	 * 发送验证码
	 */
	@Transactional
	sendCaptcha(String phone, boolean activated) {
		
		def doctor
		if (!activated) { //新用户
			doctor = doctorRepository.findByPhone(phone)
		} else { // 忘记密码发送短信
			doctor = doctorRepository.findByPhoneAndActivated(phone, activated)
			if (!doctor) {
				return '{"success" : "0", "message" : "err001"}'
			}
		}
		
		if (!doctor) {
			doctor = new Doctor(
				phone : phone
			)
			doctorRepository.save(doctor)
		}

		def doctorCaptcha = doctorCaptchaRepository.findOne(doctor.id)
		def today = new Date()
		if (doctorCaptcha) {
			def lastSentDate = doctorCaptcha.lastSentDate
			if ((today.time - lastSentDate.time) < 60000) {
				return '{"success": "0", "message": "err009"}'
			}

			if (DateUtils.isSameDay(today, doctorCaptcha.lastSentDate) && doctorCaptcha.sentNum > 10) {
				return '{"success": "0", "message": "err010"}'
			}
		}
		def captcha
		
		if (doctorCaptcha && doctorCaptcha.captcha) {
			captcha = doctorCaptcha.captcha
		} else {
			// 生成6位数字格式的验证码
			captcha = RandomStringUtils.random(1, '123456789') + RandomStringUtils.randomNumeric(5)
		}
		
		// 给指定的用户手机号发送6位随机数的验证码
		def result = smsService.sendCaptcha(phone, captcha)
		
		def doctorCaptchaLog = new DoctorCaptchaLog(
			doctor : doctor,
			captcha : captcha,
			status : result.get('resCode'),
			action : '0'
		)
		doctorCaptchaLogRepository.save(doctorCaptchaLog)
		
		if (!result.get('isSuccess')) {
			return '{"success": "0", "message": "err011"}'
		}

		if (doctorCaptcha) {
			if (DateUtils.isSameDay(today, doctorCaptcha.lastSentDate)) {
				doctorCaptcha.sentNum ++
			} else {
				doctorCaptcha.sentNum = 1
			}
			doctorCaptcha.captcha = captcha
			doctorCaptcha.lastSentDate = today
		} else {
			doctorCaptcha = new DoctorCaptcha(
				id : doctor.id,
				captcha : captcha,
				lastSentDate : today,
				sentNum : 1
			)
		}

		doctorCaptchaRepository.save(doctorCaptcha)
		'{"success": "1"}'
	
	}
	
	/**
	 * 保存udid idfa
	 * @param user
	 * @param udid
	 * @param idfa
	 * @return
	 */
	@Transactional
	saveUserCode(Doctor doctor, String udid, String idfa, String imei) {
		def code = doctorUdidRepository.findOne(doctor.id)
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
		doctorUdidRepository.save(code)
	}
	
	/**
	 * 重置密码
	 * @param userId
	 * @param password
	 * @param captcha
	 * @return
	 */
	@Transactional
	resetPassword(Doctor d, String password, String captcha) {

		if (!d) {
			return '{"success" : "0", "message": "err001"}'
		}

		def doctorCaptcha = doctorCaptchaRepository.findOne(d.id)
		if (!doctorCaptcha) {
			return '{"success": "0", "message": "err013"}'
		}

		if (captcha == doctorCaptcha.captcha) {
			d.password = DigestUtils.md5Hex(password)
			doctorRepository.save(d)
			return '{"success": "1"}'
		} else {
			return '{"success": "0", "message" : "err007"}'
		}
	}

	
}
