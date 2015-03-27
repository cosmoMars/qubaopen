package com.qubaopen.doctor.service;

import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.lang3.RandomStringUtils
import org.apache.commons.lang3.time.DateFormatUtils
import org.apache.commons.lang3.time.DateUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.qubaopen.doctor.repository.hospital.HospitalCaptchaLogRepository
import com.qubaopen.doctor.repository.hospital.HospitalCaptchaRepository
import com.qubaopen.doctor.repository.hospital.HospitalInfoRepository
import com.qubaopen.doctor.repository.hospital.HospitalRepository
import com.qubaopen.survey.entity.hospital.Hospital
import com.qubaopen.survey.entity.hospital.HospitalCaptcha
import com.qubaopen.survey.entity.hospital.HospitalCaptchaLog

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
	CaptchaService captchaService

    @Autowired
    CommonEmail commonEmail
	/**
	 * @param phone
	 * @param password
	 * @param captcha
	 * @return
	 * 医师注册
	 */
	@Transactional
	register(String url, String email, String password) {
		
		def hospital = hospitalRepository.findByEmailAndActivated(email, true)
		
		if (hospital) {
			return '{"success" : "0", "message" : "err020"}'
		}
		
		hospital = hospitalRepository.findByEmailAndActivated(email, false)
		
		if (hospital) {
			hospital.password = DigestUtils.md5Hex(password)
		} else {
			hospital = new Hospital(
				email : email,
				password : DigestUtils.md5Hex(password)
			)
		}
		
		hospital = hospitalRepository.save(hospital)
		
		sendCaptcha(url, hospital.id, email)
				
	}
	
	/**
	 * @param phone
	 * @param activated
	 * @return
	 * 发送验证码
	 */
	@Transactional
	sendCaptcha(String email) {
		
		def	hospital = hospitalRepository.findByEmailAndActivated(email, true)
		
		if (!hospital) {
			return '{"success" : "0", "message" : "err001"}'
		}
		def hospitalCaptcha = hospitalCaptchaRepository.findOne(hospital.id)
		def today = new Date()
		if (hospitalCaptcha) {
			def lastSentDate = hospitalCaptcha.lastSentDate
			if ((today.time - lastSentDate.time) < 60000) {
				return '{"success": "0", "message": "err009"}'
			}
		}
		def captcha = RandomStringUtils.random(1, '123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ') + RandomStringUtils.random(5, '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ')
		
		def result = commonEmail.sendTextMail(email, captcha)
		def hospitalCaptchaLog = new HospitalCaptchaLog(
			hospital : hospital,
			captcha : captcha,
			status : result,
			action : '0'
		)
		hospitalCaptchaLogRepository.save(hospitalCaptchaLog)
		
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
		[
			'success' : '1',
			'hospitalId' : hospital?.id	
		]
	
	}
	
	/**
	 * @param phone
	 * @param activated
	 * @return
	 * 发送验证码
	 */
	@Transactional
	sendCaptcha(String url, long hospitalId, String email) {

		def hospitalCaptcha = hospitalCaptchaRepository.findOne(hospitalId)
		def today = new Date()
		if (hospitalCaptcha) {
			def lastSentDate = hospitalCaptcha.lastSentDate
			if ((today.time - lastSentDate.time) < 60000) {
				return '{"success": "0", "message": "err009"}'
			}
		}
		def captcha = DateFormatUtils.format(new Date(), 'yyyyMMdd') + RandomStringUtils.random(22, '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ')
		
		def result = commonEmail.sendTextMail(url, hospitalId, email, captcha),
			hospitalCaptchaLog = new HospitalCaptchaLog(
			hospital : new Hospital(id : hospitalId),
			captcha : captcha,
			status : result,
			action : '0'
		)
		hospitalCaptchaLogRepository.save(hospitalCaptchaLog)
		
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
				id : hospitalId,
				captcha : captcha,
				lastSentDate : today,
				sentNum : 1
			)
		}

		hospitalCaptchaRepository.save(hospitalCaptcha)
		
		[
			'success': '1',
			'hospitalId' : hospitalId
		]
		
	}
	
	@Transactional
	sendRegisterCaptcha(String email) {
		
		def hospital = hospitalRepository.findByEmail(email)
		
		if (hospital.activated) {
			return '{"success" : "1", "message" : "该用户已经被注册"}'
		}

		def hospitalCaptcha = hospitalCaptchaRepository.findOne(hospital.id)
		
		def today = new Date()
		if (hospitalCaptcha) {
			def lastSentDate = hospitalCaptcha.lastSentDate
			if ((today.time - lastSentDate.time) < 60000) {
				return '{"success": "0", "message": "err009"}' // 1分钟内不能连续发送
			}

			if (DateUtils.isSameDay(today, hospitalCaptcha.lastSentDate) && hospitalCaptcha.sentNum > 10) {
				return '{"success": "0", "message": "err010"}' // 每天不能超过10次
			}
			if (today.before(hospitalCaptcha.expiredTime)) {
				return '{"success": "0", "message": "超时"}' // 15mins 超时
			}
			
		}
		
		// 生成30位验证码
		def captcha = DateFormatUtils.format(new Date(), 'yyyyMMdd') + RandomStringUtils.random(22, '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ')
		
		def result = commonEmail.sendTextMail(email, captcha)
		
		def hospitalCaptchaLog = new HospitalCaptchaLog(
			hospital : hospital,
			captcha : captcha,
			status : result,
			action : '0'
		)
		hospitalCaptchaLogRepository.save(hospitalCaptchaLog)
		
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
		
		def cal = Calendar.getInstance()
		cal.setTime today
		cal.add(Calendar.MINUTE, 15)
		hospitalCaptcha.expiredTime = cal.getTime()

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
	resetPassword(Hospital h, String password) {

		if (!h) {
			return '{"success" : "0", "message": "err001"}'
		}

		h.password = DigestUtils.md5Hex(password)
		hospitalRepository.save(h)
		return '{"success": "1"}'
	}
}
