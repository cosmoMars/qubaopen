package com.qubaopen.doctor.controller.cash;

import groovy.json.JsonSlurper

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttributes

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.doctor.controller.doctor.DoctorController;
import com.qubaopen.doctor.repository.cash.CashLogRepository
import com.qubaopen.doctor.repository.cash.CashRepository
import com.qubaopen.doctor.repository.cash.TakeCashRepository;
import com.qubaopen.doctor.repository.doctor.DoctorCaptchaRepository
import com.qubaopen.doctor.repository.doctor.DoctorIdCardBindRepository
import com.qubaopen.doctor.repository.doctor.DoctorRepository;
import com.qubaopen.doctor.service.DoctorIdCardBindService
import com.qubaopen.doctor.service.DoctorService;
import com.qubaopen.doctor.service.SmsService;
import com.qubaopen.survey.entity.cash.Bank
import com.qubaopen.survey.entity.cash.Cash
import com.qubaopen.survey.entity.cash.CashLog
import com.qubaopen.survey.entity.cash.TakeCash
import com.qubaopen.survey.entity.doctor.Doctor

@RestController
@RequestMapping('cash')
@SessionAttributes('currentDoctor')
public class CashController extends AbstractBaseController<Cash, Long> {

	static Logger logger = LoggerFactory.getLogger(CashController.class)
	
	@Autowired
	CashRepository cashRepository
	
	@Autowired
	CashLogRepository cashLogRepository
	
	@Autowired
	DoctorCaptchaRepository doctorCaptchaRepository
	
	@Autowired
	DoctorIdCardBindService doctorIdCardBindService
	
	@Autowired
	DoctorIdCardBindRepository  doctorIdCardBindRepository
	
	@Autowired
	TakeCashRepository takeCashRepository
	
	@Autowired
	DoctorService doctorService
	
	@Autowired
	DoctorRepository doctorRepository

	@Override
	MyRepository<Cash, Long> getRepository() {
		cashRepository
	}
	
	/**
	 * 获取医师现金信息
	 * @param doctor
	 * @return
	 */
	@RequestMapping(value = 'retrieveCashInfo', method = RequestMethod.GET)
	retrieveCashInfo(@ModelAttribute('currentDoctor') Doctor doctor) {

		logger.trace '--  获取医师现金信息 --'
		
		def cash = cashRepository.findOne(doctor.id),
			cashLogs = cashLogRepository.findByDoctorOrderByCreatedDateDesc(doctor)
			
		def inDetail = [], outDetail = []
		
		cashLogs.each {
			if (CashLog.Type.In == it.type) {
				inDetail << it.detail
			} else if (CashLog.Type.Out == it.type) {
				outDetail << it.detail
			}
		}
		
		[
			"success" : "1",
			'inCash' : cash?.inCash,
			'outCash' : cash?.outCash,
			'currentCash' : cash?.currentCash,
			'inDetail' : inDetail,
			'outDetail' : outDetail
		]
	}
	
	/**
	 * 提现
	 * @param curCash
	 * @param bankId
	 * @param bankCard
	 * @param name
	 * @param idCard
	 * @param captcha
	 * @param doctor
	 * @return
	 */
	@Transactional
	@RequestMapping(value = 'takeCash', method = RequestMethod.POST)
	takeCash(@RequestParam(required = false) Double curCash,
		@RequestParam(required = false) Integer type,
		@RequestParam(required = false) String alipayNum,
		@RequestParam(required = false) Long bankId,
		@RequestParam(required = false) String bankCard,
		@RequestParam(required = false) String name,
		@RequestParam(required = false) String idCard,
		@RequestParam(required = false) String captcha,
		@ModelAttribute('currentDoctor') Doctor doctor) {
		
		logger.trace '-- 提现请求 --'
		
		// 验证码对比
		def dCaptcha = doctorCaptchaRepository.findOne(doctor.id)
		
		if (captcha != dCaptcha.captcha) {
			return '{"success" : "0", "message" : "err007"}'
		}
		dCaptcha.captcha = null
		doctorCaptchaRepository.save(dCaptcha)
		
		// 身份证绑定
		def idCardBind = doctorIdCardBindRepository.findByDoctor(doctor),
			result
		
		if (!idCardBind) {
			result = doctorIdCardBindService.submitUserIdCard(idCard, name, doctor)
		}
		if (result) {
			def json = (Map<String, ?>) new JsonSlurper().parseText(result)
			if ('1' != json['success'] || !'1'.equals(json['success'])) {
				return result
			}
		} else {
			return '{"success" : "0", "message" : "err900"}' // 该医师还未认证
		}
		
		def cash = cashRepository.findOne(doctor.id)
		if (type == null) {
			return '{"success" : "0", "message" : "err901"}' // 支付方式不正确
		}
		if (curCash != null && curCash > cash.currentCash) {
			return '{"success" : "0", "message" : "err911"}' // 取款金额超过当前余额
		}
		
		cash.currentCash -= curCash
		cash.outCash += curCash
		
		def takeCash = new TakeCash(
			doctor : doctor,
			cash : curCash,
			status : TakeCash.Status.Auditing
		)
		if (TakeCash.Type.Alipay == TakeCash.Type.values[type]) {
			takeCash.type = TakeCash.Type.Alipay
			if (!alipayNum) {
				return '{"success" : "0", "message" : "err902"}' // 支付宝帐号为空
			}
			takeCash.alipayNum = alipayNum
			
		} else if (TakeCash.Type.BackCard == TakeCash.Type.values[type]) {
			takeCash.type = TakeCash.Type.BackCard
			
			if (bankId == null) {
				return '{"success" : "0", "message" : "err912"}' // 没有选择银行
			}
			if (bankCard == null) {
				return '{"success" : "0", "message" : "err913"}' // 没有银行卡号
			}
			takeCash.bank = new Bank(id : bankId)
			takeCash.bankCard = bankCard
		}
		cashRepository.save(cash)
		takeCashRepository.save(takeCash)
		'{"success" : "1"}'
	}
	
	/**
	 * @param cashId
	 * @param status
	 * @param failureReason
	 * @return
	 * 修改提款状态
	 */
	@RequestMapping(value = 'modifyTakeCashStatus', method = RequestMethod.POST)
	modifyTakeCashStatus(@RequestParam(required = false) Long cashId,
		@RequestParam(required = false) Integer status,
		@RequestParam(required = false) String failureReason) {
		
		def takeCash = takeCashRepository.findOne(cashId)
		
		if (status == null) {
			return '{"success" : "0", "message" : "err914"}' // 状态位不正确
		}
		
		if (TakeCash.Status.Success == TakeCash.Status.values()[status]) {
			takeCash.status = TakeCash.Status.Success
		}
		if (TakeCash.Status.Failure == TakeCash.Status.values()[status]) {
			if (failureReason == null) {
				return '{"success" : "0", "message" : "err915"}' // 没有失败理由
			}
			takeCash.status = TakeCash.Status.Failure
			takeCash.failureReason = failureReason
		}
		takeCashRepository.save(takeCash)
		'{"success" : "1"}'
	}
	
	/**
	 * @param doctor
	 * @return
	 * 提现验证码发送
	 */
	@RequestMapping(value = 'retireveCashCaptcha', method = RequestMethod.GET)
	retireveCashCaptcha(@ModelAttribute('currentDoctor') Doctor doctor){
		 
		def phone = doctor.phone
		if (!phone) {
			def d = doctorRepository.findOne(doctor.id)
			phone = d.phone
		}
		
		doctorService.sendCaptcha(phone, true)
	}
	
}
