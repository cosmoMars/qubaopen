package com.qubaopen.doctor.service;

import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.lang3.RandomStringUtils
import org.apache.commons.lang3.time.DateUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.qubaopen.doctor.repository.hospital.HospitalCaptchaLogRepository
import com.qubaopen.doctor.repository.hospital.HospitalCaptchaRepository
import com.qubaopen.doctor.repository.hospital.HospitalInfoRepository
import com.qubaopen.doctor.repository.hospital.HospitalRepository
import com.qubaopen.doctor.repository.hospital.HospitalUdidRepository
import com.qubaopen.doctor.utils.DateCommons
import com.qubaopen.survey.entity.doctor.Doctor;
import com.qubaopen.survey.entity.hospital.Hospital
import com.qubaopen.survey.entity.hospital.HospitalCaptcha
import com.qubaopen.survey.entity.hospital.HospitalCaptchaLog
import com.qubaopen.survey.entity.hospital.HospitalInfo
import com.qubaopen.survey.entity.hospital.HospitalUdid

@Service
public class HospitalService {

	@Autowired
	HospitalRepository hospitalRepository
	
	@Autowired
	HospitalInfoRepository hospitalInfoRepository
	
	@Autowired
	HospitalCaptchaRepository hospitalCaptchaRepository
	
	@Autowired
	HospitalCaptchaLogRepository hospitalCaptchaLogRepository
	
	@Autowired
	HospitalUdidRepository hospitalUdidRepository
	
	/**
	 * @param user
	 * @param udid
	 * @param idfa
	 * @return
	 */
	@Transactional
	saveUserCode(Hospital hospital, String udid, String idfa, String imei) {
		
		def code = hospitalUdidRepository.findOne(hospital.id)
		
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
		hospitalUdidRepository.save(code)
	}
	
	/**
	 * @param phone
	 * @param password
	 * @param captcha
	 * @return
	 * 医师注册
	 */
	@Transactional
	register(String email, String password, String captcha) {
		
		def h = hospitalRepository.findByEmail(email)
		if (h) {
			if (h.activated) {
				return '{"success": "0", "message": "err006"}'
			}
			
			def hospitalCaptchaLog = new HospitalCaptchaLog(
				hospital : h,
				captcha : captcha,
				action : '1'
			)
			def hospitalCaptcha = hospitalCaptchaRepository.findOne(h.id)
			if (hospitalCaptcha?.captcha != captcha) {
				return '{"success": "0", "message": "err007"}'
			}
			hospitalCaptcha.captcha = null
			hospitalCaptchaRepository.save(hospitalCaptcha)
			
			h.password = DigestUtils.md5Hex(password)
			h.activated = true
			
			h = hospitalRepository.save(h)
			
			def hospitalInfo = new HospitalInfo(
				id : h.id
			)
			def hospitalUdid = new HospitalUdid(
				id : h.id,
				startTime : DateCommons.String2Date('09:00','HH:mm'),
				endTime : DateCommons.String2Date('22:00','HH:mm'),
				push : true
			)
			hospitalInfoRepository.save(hospitalInfo)
			hospitalUdidRepository.save(hospitalUdid)
			return [
				'success': '1',
				'doctorId' : h.id
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
	sendCaptcha(String email, boolean activated) {
		
		def hospital
		if (!activated) { //新用户
			hospital = hospitalRepository.findByEmail(email)
		} else { // 忘记密码发送短信
			hospital = hospitalRepository.findByEmailAndActivated(email, activated)
			if (!hospital) {
				return '{"success" : "0", "message" : "err001"}'
			}
		}
		
		if (!hospital) {
			hospital = new Hospital(
				phone : email
			)
			hospitalRepository.save(hospital)
		}

		def hospitalCaptcha = hospitalCaptchaRepository.findOne(hospital.id)
		def today = new Date()
		if (hospitalCaptcha) {
			def lastSentDate = hospitalCaptcha.lastSentDate
			if ((today.time - lastSentDate.time) < 60000) {
				return '{"success": "0", "message": "err009"}'
			}

			if (DateUtils.isSameDay(today, hospitalCaptcha.lastSentDate) && hospitalCaptcha.sentNum > 10) {
				return '{"success": "0", "message": "err010"}'
			}
		}
		def captcha
		
		if (hospitalCaptcha && hospitalCaptcha.captcha) {
			captcha = hospitalCaptcha.captcha
		} else {
			// 生成6位数字格式的验证码
			captcha = RandomStringUtils.random(1, '123456789') + RandomStringUtils.randomNumeric(5)
		}
		
		// 给指定的用户手机号发送6位随机数的验证码
//		def result = smsService.sendCaptcha(email, captcha)
		
		def hospitalCaptchaLog = new HospitalCaptchaLog(
			hospital : hospital,
			captcha : captcha,
			status : result.get('resCode'),
			action : '0'
		)
		hospitalCaptchaLogRepository.save(hospitalCaptchaLog)
		
		if (!result.get('isSuccess')) {
			return '{"success": "0", "message": "err011"}'
		}

		if (hospitalCaptcha) {
			if (DateUtils.isSameDay(today, hospitalCaptcha.lastSentDate)) {
				hospitalCaptcha.sentNum ++
			} else {
				hospitalCaptcha.sentNum = 1
			}
			hospitalCaptcha.captcha = captcha
			hospitalCaptcha.lastSentDate = today
		} else {
			hospitalCaptcha = new HospitalCaptcha(
				id : hospital.id,
				captcha : captcha,
				lastSentDate : today,
				sentNum : 1
			)
		}

		hospitalCaptchaRepository.save(hospitalCaptcha)
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
	resetPassword(Hospital h, String password, String captcha) {

		if (!h) {
			return '{"success" : "0", "message": "err001"}'
		}

		def hospitalCaptcha = hospitalCaptchaRepository.findOne(h.id)
		if (!hospitalCaptcha) {
			return '{"success": "0", "message": "err013"}'
		}

		if (captcha == hospitalCaptcha.captcha) {
			h.password = DigestUtils.md5Hex(password)
			hospitalRepository.save(h)
			return '{"success": "1"}'
		} else {
			return '{"success": "0", "message" : "err007"}'
		}
	}
}
