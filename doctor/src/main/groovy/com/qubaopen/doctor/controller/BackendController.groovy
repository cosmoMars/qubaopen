package com.qubaopen.doctor.controller;

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import com.qubaopen.doctor.repository.cash.DoctorCashLogRepository;
import com.qubaopen.doctor.repository.cash.DoctorCashRepository;
import com.qubaopen.doctor.repository.cash.DoctorTakeCashRepository
import com.qubaopen.survey.entity.cash.DoctorCashLog
import com.qubaopen.survey.entity.cash.DoctorTakeCash

@RestController
@RequestMapping('backend')
public class BackendController {
	
	@Autowired
	DoctorTakeCashRepository doctorTakeCashRepository
	
	@Autowired
	DoctorCashLogRepository doctorCashLogRepository
	
	@Autowired
	DoctorCashRepository doctorCashRepository
	
	
	/**
	 * @param cashId
	 * @param status
	 * @param failureReason
	 * @return
	 * 修改提款状态
	 */
	@RequestMapping(value = 'modifyTakeCashStatus', method = RequestMethod.POST)
	modifyTakeCashStatus(@RequestParam(required = false) Long cashId,
		@RequestParam(required = false) Long doctorCashId,
		@RequestParam(required = false) Integer status,
		@RequestParam(required = false) String failureReason) {
		
		def takeCash = doctorTakeCashRepository.findOne(cashId)
		
		def cashLog = doctorCashLogRepository.findByDoctorTakeCash(new DoctorTakeCash(id : cashId)),
			doctorCash = doctorCashRepository.findOne()
		
		if (status == null) {
			return '{"success" : "0", "message" : "err914"}' // 状态位不正确
		}
		
		if (DoctorTakeCash.Status.Success.ordinal() == status) {
			
			takeCash.status = DoctorTakeCash.Status.Success
			
			cashLog.payStatus = DoctorCashLog.PayStatus.Completed

			doctorCashLogRepository.save(cashLog)
		}
		if (DoctorTakeCash.Status.Failure.ordinal() == status) {
			if (failureReason == null) {
				return '{"success" : "0", "message" : "err915"}' // 没有失败理由
			}
			takeCash.status = DoctorTakeCash.Status.Failure
			takeCash.failureReason = failureReason
			
			cashLog.payStatus = DoctorCashLog.PayStatus.Failure
			doctorCashLogRepository.save(cashLog)
		}
	}
		
}
