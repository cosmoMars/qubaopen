package com.qubaopen.doctor.controller.cash;

import groovy.json.JsonSlurper

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttributes

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.doctor.repository.cash.CashLogRepository
import com.qubaopen.doctor.repository.cash.CashRepository
import com.qubaopen.doctor.repository.doctor.DoctorIdCardBindRepository
import com.qubaopen.doctor.service.DoctorIdCardBindService
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
	DoctorIdCardBindService doctorIdCardBindService
	
	@Autowired
	DoctorIdCardBindRepository  doctorIdCardBindRepository

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
	@RequestMapping(value = 'alipayCash', method = RequestMethod.POST)
	alipayCash(@RequestParam(required = false) Double curCash,
		@RequestParam(required = false) Integer type,
		@RequestParam(required = false) String alipayNum,
		@RequestParam(required = false) Long bankId,
		@RequestParam(required = false) String bankCard,
		@RequestParam(required = false) String name,
		@RequestParam(required = false) String idCard,
		@RequestParam(required = false) String captcha,
		@ModelAttribute('currentDoctor') Doctor doctor) {
		
		logger.trace '-- 提现请求 --'
		
		def idCardBind = doctorIdCardBindRepository.findByDoctor(doctor),
			result
			
		def cash = cashRepository.findOne(doctor.id)
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
		
		if (!type) {
			return '{"success" : "0", "message" : "err901"}' // 支付方式不正确
		}
		if (TakeCash.Type.Alipay == TakeCash.Type.values[type]) {
			if (!alipayNum) {
				return '{"success" : "0", "message" : "err902"}' // 支付宝帐号为空
			}
//			if (curCash != null && curCash > )
			
		}
	}
	
}
